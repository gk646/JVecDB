/*
 * MIT License
 *
 * Copyright (c) 2023 <Lukas Gilch>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package gk646.jvecdb.utils.datastructures;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;


public final class Vector<T> extends AbstractList<T> implements RandomAccess, Iterable<T> {
    private int size = 0;
    private T[] elements;

    @SuppressWarnings("unchecked")
    public Vector(int initialCapacity) {
        elements = (T[]) new Object[initialCapacity];
    }

    public Vector() {
        this(10);
    }

    @Override
    public boolean add(T obj) {
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, elements.length * 3);
        }
        elements[size++] = obj;
        return true;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        T removedElement = elements[index];
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null;
        return removedElement;
    }

    public int size() {
        return size;
    }

    public T get(int index) {
        return elements[index];
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return elements[currentIndex++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        elements = (T[]) new Object[10];
        size = 0;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        int span = toIndex - fromIndex;
        Vector<T> result = new Vector<>(span);
        result.size = span;
        System.arraycopy(this.elements, fromIndex, result.elements, 0, span);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Vector<?>)) {
            return false;
        }

        Vector<T> vector = (Vector<T>) obj;

        if (vector.size == size) {
            for (int i = 0; i < size; i++) {
                if (!vector.get(i).equals(elements[i])) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    @Override
    public T set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        T oldValue = elements[index];
        elements[index] = element;
        return oldValue;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < size; i++) {
            result = 31 * result + (elements[i] == null ? 0 : elements[i].hashCode());
        }
        return result;
    }
}


