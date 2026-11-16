import pathlib
import subprocess
import tempfile
import unittest

ROOT = pathlib.Path(__file__).resolve().parents[1]


class ConsoleTests(unittest.TestCase):
    def run_app(self, path, commands):
        return subprocess.run(["java", "-cp", str(ROOT / "build"), "Main", str(path)],
                              input=commands, text=True, capture_output=True, check=True)

    def test_restart_preserves_added_and_borrowed_items(self):
        with tempfile.TemporaryDirectory() as directory:
            path = pathlib.Path(directory) / "library.tsv"
            self.run_app(path, "6\nB3\nUus teos\nMari Õis\n4\nB3\n0\n")
            output = self.run_app(path, "8\nMari\n10\n0\n").stdout
            self.assertIn("B3 | Uus teos", output)
            self.assertIn("laenutatud: 1", output)

    def test_rejected_removal_is_still_present_after_restart(self):
        with tempfile.TemporaryDirectory() as directory:
            path = pathlib.Path(directory) / "library.tsv"
            output = self.run_app(path, "4\nB1\n9\nB1\n0\n").stdout
            self.assertIn("Laenutatud teost ei saa eemaldada", output)
            self.assertIn("B1 |", self.run_app(path, "1\n0\n").stdout)

    def test_corrupt_file_is_not_replaced(self):
        with tempfile.TemporaryDirectory() as directory:
            path = pathlib.Path(directory) / "library.tsv"
            path.write_text("corrupt")
            result = self.run_app(path, "0\n")
            self.assertIn("Andmeid ei saanud avada", result.stderr)
            self.assertEqual(path.read_text(), "corrupt")

    def test_successful_removal_is_persistent(self):
        with tempfile.TemporaryDirectory() as directory:
            path = pathlib.Path(directory) / "library.tsv"
            self.run_app(path, "9\nM1\n0\n")
            result = self.run_app(path, "10\n0\n")
            self.assertIn("Kokku: 2", result.stdout)
            self.assertIn("ajakirjad: 0", result.stdout)


if __name__ == "__main__": unittest.main()
