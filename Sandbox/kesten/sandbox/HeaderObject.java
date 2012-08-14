package kesten.sandbox;

interface HeaderInterface {
	int getSize();
}

public class HeaderObject implements HeaderInterface {

	int mSize;

	public int getSize() {
		return mSize;
	}

}
