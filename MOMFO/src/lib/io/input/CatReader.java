package lib.io.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.SequenceInputStream;
import java.util.ArrayDeque;

import lib.lang.PathTreat;

/**
 * CatReader reads multiple files by concatenating them.
 * When all the characters in a file are read, this reader try to read
 * characters from another specified file. You can specify files from methods
 * and a line in a file.
 * <p>
 * To specify another file to be read in a file, put the line of format
 * "&amp;include[ \t]+<i>filePath</i>[\r]\n" in a file to be read. The
 * <i>filePath</i> can contain white spaces and tabs. <i>filePath</i> can be
 * either a relative path or an absolute path. If a relative path is specified,
 * the new file (specified in <i>filePath</i>) is searched from the currently
 * reading file.
 *
 * @see SequenceInputStream
 */
public class CatReader extends Reader {
	public static final String INCLUDE = "&include";
	private static final char[] SEQUENCE = INCLUDE.toCharArray();
	private static final int WORD_LENGTH = INCLUDE.length();

	private static final char SPACE = ' ';
	private static final char TAB = '\t';
	private static final int EOF = -1;

	private PushbackReader reader_;

	private ArrayDeque<File> includedFiles_ = new ArrayDeque<File>();
	private File currentReading_;

	/**
	 * The base directory is set as the current directory.
	 * @param in
	 */
	public CatReader(Reader in) {
		this(in, PathTreat.getCurrentDir());
	}

	public CatReader(Reader in, File baseDir) {
		super();
		currentReading_ = baseDir;
		reader_ = new PushbackReader(in, WORD_LENGTH - 1);
	}

	public CatReader(String ... files) throws IOException {
		super();
		currentReading_ = new File(files[0]);
		reader_ = new PushbackReader(new BufferedReader(new FileReader(currentReading_)), WORD_LENGTH - 1);
		for (int i = 1; i < files.length; i++) {
			includedFiles_.add(new File(files[i]));
		}
	}

	public CatReader(File ... files) throws IOException {
		super();
		currentReading_ = files[0];
		reader_ = new PushbackReader(new BufferedReader(new FileReader(currentReading_)), WORD_LENGTH - 1);
		for (int i = 1; i < files.length; i++) {
			includedFiles_.add(files[i]);
		}
	}

	/**
	 * @return The number of characters read, or -1
	 * if the end of the stream has been reached (including when the stream has been closed).
	 */
	@Override
	public int read(char[] cbuf, int off, int length) throws IOException {
		if (currentReading_ == null) return -1;
		int orgOff = off;
		int len = WORD_LENGTH;
		synchronized (lock) {
		// When the number of read characters is larger than the length of INCLUDE,
		// INCLUDE is searched using cbuf by Boyer-Moore method.
		NEXT_CHARS:
		while (len <= length) {
			int nRead = reader_.read(cbuf, off, len);
			if (nRead != -1) {
				off += nRead;
				length -= nRead;
			}

			// when read through one file
			if(len != nRead) {
				if (nextFile()) {
					len = WORD_LENGTH;
					continue;
				} else {
					return off - orgOff;
				}
			}

			// check whether INCLUDE can not be appear in the read characters
			if (cbuf[off - 1] != 'e') {
				len = shift(cbuf[off - 1]);
				continue;
			}

			// check matching
			for (int i = 1; i < WORD_LENGTH; i++) {
				if (cbuf[off - 1 - i] != SEQUENCE[WORD_LENGTH - 1 - i]) {
					len = WORD_LENGTH;
					continue NEXT_CHARS;
				}
			}

			// INCLUDE is found
			off -= WORD_LENGTH;
			length += WORD_LENGTH;
			openFile();
			len = WORD_LENGTH;
		}

		// length is shorter than INCLUDE
		if (len != WORD_LENGTH) {
			/* If some part of the INCLUDE is already matched,
			 * they are pushed back in order to simplify
			 * implementation of readRest().
			 */
			int back = WORD_LENGTH - len;
			off -= back;
			length += back;
			reader_.unread(cbuf, off, back);
		}
		return readRest(cbuf, off, length) + off - orgOff;
		}
	}

	/**
	 * Checks whether SEQUENCE will appear in the stream from the current position.
	 * This method reads no more than WORD_LENGTH characters from the stream
	 * to search SEQUENCE.
	 * This method is invoked when no more than WORD_LENGTH characters are not
	 * allowed to map cbuf.
	 *
	 * @param cbuf
	 * @param off
	 * @param length
	 * @return The number of character assigned to off in this method, or
	 * -1 if the end of the stream has been reached.
	 * @throws IOException
	 */
	private int readRest(char[] cbuf, int off, int length) throws IOException {
		assert length < WORD_LENGTH;

		for (int i = 0; i < WORD_LENGTH; i++) {
			int read = reader_.read();
			if (read == SEQUENCE[i]) continue;

			System.arraycopy(SEQUENCE, 0, cbuf, off, Math.min(length, i));
			if (read == -1) {
				// the end of a stream has been reached
				if (nextFile()) {
					return i + readRest(cbuf, off + i, length - i);
				} else {
					return i;
				}
			} else {
				// Not match
				if (length <= i) {
					// enough characters are read
					reader_.unread(read);
					reader_.unread(SEQUENCE, 0, i - length);
					return length;
				} else {
					cbuf[off + i] = (char) read;
					if (length == i - 1) {
						// just the number of requested characters are read
						return length;
					} else {
						// need to read more characters
						return i + 1 + readRest(cbuf, off + i + 1, length - i - 1);
					}
				}
			}
		}
		// INCLUDE is found
		openFile();
		return readRest(cbuf, off, length);
	}

	/** Shift table for Boyer-Moore string search */
	private static final int shift(char c) {
		switch(c) {
		case '&':
			return WORD_LENGTH -1;
		case 'i':
			return WORD_LENGTH -2;
		case 'n':
			return WORD_LENGTH -3;
		case 'c':
			return WORD_LENGTH -4;
		case 'l':
			return WORD_LENGTH -5;
		case 'u':
			return WORD_LENGTH -6;
		case 'd':
			return WORD_LENGTH -7;
		case 'e':
			assert false;
			return WORD_LENGTH -8;
		default:
			return WORD_LENGTH;
		}
	}

	/**
	 * Parses the stream and stacks the specified file.
	 * @throws IOException
	 */
	private void openFile() throws IOException {
		skipDelimiter();
		stackFile(readFileName().toString());
	}

	/**
	 * Reads all delimiters between SEQUENCE and a file path.
	 * @throws IOException
	 */
	private void skipDelimiter() throws IOException {
		int read;
		while(true) {
			read = reader_.read();
			if (read == EOF || read == '\n') throw new IllegalArgumentException(
				"Include sentence must be "+ INCLUDE +"[ \\t]+filename[\\r]\\n");
			if (read != SPACE && read != TAB) {
				reader_.unread(read);
				return;
			}
		}
	}

	/**
	 * Parses file path.
	 * This method must be called after skipDelimiters.
	 * @return the specified file path
	 * @throws IOException
	 */
	private StringBuffer readFileName() throws IOException {
		int read;
		StringBuffer retval = new StringBuffer();
		while(true) {
			read = reader_.read();
			if (read == EOF || read == '\n') {
				return retval;
			} else if (read != '\r') {
				retval.append((char) read);
			}
		}
	}

	/**
	 * Reads a line from the currently reading file or stacked file.
	 * <p>
	 * If read &include sentence is wrong OutOfBoundaryException is thrown.
	 *
	 * @return the next line or the first line of stacked line or null.
	 * null is returned when all files are read.
	 * @throws IOException
	 */
	public String readLine() throws IOException {
		String line = readLine0();
		// reads through one file
		if (line == null) {
			if (nextFile()) return readLine();
			return null;
		}

		int pos = line.indexOf(INCLUDE);
		if (pos != -1) {
			stackFile(parseInclude(line.substring(pos)));
			if (pos == 0) {
				// if the line starts with INCLUDE, reads another line
				return readLine();
			} else {
				// if INCLUDE is not at the head of line, another line is not read.
				return line.substring(0, pos);
			}
		}
		return line;
	}

	/**
	 * Different from readFileName(), this method is called only from readLine().
	 * @throws IndexOutOfBoundsException when &include sentence is wrong
	 */
	private String parseInclude(String includeSentence) {
		// get file path to be included
		int i = INCLUDE.length() - 1;
		char c;
		try {
			do {
				c = includeSentence.charAt(++i);
			} while (c == SPACE || c == TAB);
		} catch (IndexOutOfBoundsException obe) {
			throw new IllegalArgumentException(
					"Include sentence format is wrong: "+ includeSentence
					+" in "+ currentReading_ +" is illegal format.");
		}
		return includeSentence.substring(i);
	}

	/**
	 * Reads one line. This private method do not consider INCLUDE.
	 * @return
	 * @throws IOException
	 */
	private String readLine0() throws IOException {
		StringBuffer line = new StringBuffer();

		boolean carrigeReturn = false;
		int c;
		while(true) {
			c = reader_.read();
			if (c == '\n' || c == -1) break;
			if (c == '\r') carrigeReturn = true;
			else carrigeReturn = false;
			line.append((char) c);
		}

		if (c == -1 && line.length() == 0) return null;
		if (carrigeReturn) line.deleteCharAt(line.length() - 1);
		return line.toString();
	}


	/** stacks included file */
	private void stackFile(String path) {
		File include = new File(path);
		// convert relative path to absolute path
		if (!include.isAbsolute()) {
			if (currentReading_.isDirectory()) {
				include = new File(currentReading_, path);
			} else {
				include = new File(currentReading_.getParentFile(), path);
			}
		}
		includedFiles_.add(include);
	}

	/** Starts to read from a next file.
	 * @return True if new file is set, or false if there are no file specified.
	 */
	public boolean nextFile() throws IOException {
		synchronized (lock) {
			reader_.close();
			currentReading_ = includedFiles_.pollFirst();
			if (currentReading_ == null) return false;
			reader_ = new PushbackReader(new BufferedReader(new FileReader(currentReading_)), WORD_LENGTH - 1);
			return true;
		}
	}

	public void addFile(String path) {
		addFile(new File(path));
	}

	public void addFile(File file) {
		synchronized (includedFiles_) {
			includedFiles_.add(file);
		}
	}

	@Override
	public boolean ready() throws IOException {
		synchronized (lock) {
			if (currentReading_ != null && reader_.ready()) return true;
			if (!nextFile()) return false;
			return reader_.ready();
		}
	}

	@Override
	public void close() throws IOException {
		synchronized (lock) {
			reader_.close();
			includedFiles_.clear();
			currentReading_ = null;
		}
	}
}
