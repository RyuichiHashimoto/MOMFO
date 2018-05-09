package lib.Structure;

import static lib.lang.Generics.*;

import java.util.Arrays;
import java.util.Comparator;

import lib.lang.Generics;
import lib.util.ArrayUtility;

public class ObjectStack<T> implements Cloneable {
	protected int index_;
	protected T[] array_;

	public ObjectStack(T[] stack, int length) {
		array_ = stack;
		index_ = length;
	}

	public void push(T value) {
		array_[index_++] = value;
	}

	public T pop() {
		return array_[--index_];
	}

	/**
	 *  Add an element to the tail of the array.
	 *  Different from {@link #push(T)}, the capacity of the array
	 *  is automatically grown if necessary.
	 * @param value
	 */
	public void add(T value) {
		ensureCapacity(size() + 1);
		push(value);
	}

	public void clear() {
		index_ = 0;
	}

	public T get(int idx) {
		return array_[idx];
	}

	public T peekLast() {
		return array_[index_ - 1];
	}

	public boolean contains(T v) {
		for (int i = 0; i < index_; i++) {
			if (isEqual(array_[i], v)) return true;
		}
		return false;
	}

	public int indexOf(T v) {
		for (int i = 0; i < index_; i++) {
			if (isEqual(array_[i], v)) return i;
		}
		return -1;
	}

	public int indexOf(T v, Comparator<? super T> cmp) {
		for (int i = 0; i < index_; i++) {
			if (cmp.compare(array_[i], v) == 0) return i;
		}
		return -1;
	}

	public void swap(int i, int j) {
		T swp = array_[i];
		array_[i] = array_[j];
		array_[j] = swp;
	}

	public void swapRemove(int idx) {
		array_[idx] = pop();
	}

	public void remove(int idx) {
		remove(idx, 1);
	}

	public void remove(int idx, int length) {
		shift(idx + length, index_ - idx - length, idx);
		index_ -= length;
	}

	/**
	 * <i>length</i> elements from the position <i>start</i> are moved to
	 * <i>dest</i>. The elements from the <i>dest</i> are overwritten.
	 * @param start
	 * @param length
	 * @param dest
	 */
	public void shift(int start, int length, int dest) {
		System.arraycopy(array_, start, array_, dest, length);
	}

	public void sort(Comparator<? super T> c) {
		Arrays.sort(array_, 0, index_, c);
	}

	public int size() {
		return index_;
	}

	public int capacity() {
		return array_.length;
	}

	public boolean isEmpty() {
		return index_ == 0;
	}

	public boolean isFull() {
		return index_ == array_.length;
	}

	public void ensureCapacity(int size) {
		EXTEND_ARRAY:
		if (capacity() < size) {
			if (size < 16) {
				grow(16);
				break EXTEND_ARRAY;
			}

			int i = 1;
			int newCapacity = Math.max(16, capacity());;
			do {
				newCapacity += (newCapacity >> i);
				i++;
			} while (newCapacity < size);
			grow(newCapacity);
		}
	}

	public void grow(int newSize) {
		if (newSize <= array_.length) return;
		array_ = Arrays.copyOf(array_, newSize);
	}

	public T[] get() {
		return array_;
	}

	@Override
	public String toString() {
		System.err.println("error");
		return null;
		//	return ArrayUtility.toCSV(array_);
	}
	public void paste(T[] array, int offset) {
		System.arraycopy(array_, 0, array, offset, size());
	}

	public T[] toArray() {
		if (isFull()) {
			return array_.clone();
		} else {
			T[] retval = Generics.cast(Generics.newArray(index_, array_));
			System.arraycopy(array_, 0, retval, 0, index_);
			return retval;
		}
	}

	@Override
	public ObjectStack<T> clone() {
		try {
			ObjectStack<T> retval = Generics.cast(super.clone());
			retval.array_ = array_.clone();
			return retval;
		} catch(CloneNotSupportedException ce) {
			throw new InternalError(ce.getMessage());
		}
	}

	public ObjectStack<T> trimmedClone(){
		return new ObjectStack<T>(Arrays.copyOf(array_, index_), index_);
	}

	public ObjectStack<T> trimmedClone(int from, int to) {
		return new ObjectStack<T>(Arrays.copyOfRange(array_, from, to), to - from);
	}
}
