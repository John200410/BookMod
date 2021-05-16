package org.rusherhack.bookmod.util;

/**
 * Object used to store a book's name and author
 *
 * @author John200410 5/15/2021 for bookmod
 */
public class BookInfo {
	
	/**
	 * Title of the book
	 */
	private final String title;
	
	/**
	 * Book author
	 */
	private final String author;
	
	public BookInfo(String title, String author) {
		this.title = title;
		this.author = author;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getAuthor() {
		return author;
	}
	
	@Override
	public int hashCode() {
		return this.title.hashCode() + this.author.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof BookInfo)) {
			return false;
		} else {
			final BookInfo altObj = (BookInfo) obj;
			return this.title.equals(altObj.title) && this.author.equals(altObj.author);
		}
	}
	
	@Override
	public String toString() {
		final boolean useQuotes = this.title.contains(" ");
		
		final String title = useQuotes ? String.format("\"%s\"", this.title) : this.title;
		return title + " " + this.author;
	}
}
