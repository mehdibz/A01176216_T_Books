/**
 * Project: Books
 * File: BooksReport.java
 */

package a00123456.book.io;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00123456.book.BookOptions;
import a00123456.book.data.AllData;
import a00123456.book.data.Book;
import a00123456.book.data.util.Common;

/**
 * @author Sam Cirka, A00123456
 *
 */
public class BooksReport {

    public static final String REPORT_FILENAME = "book_report.txt";
    public static final String HORIZONTAL_LINE = "----------------------------------------------------------------------------------";
    public static final String HEADER_FORMAT = "%-8s %-12s %-40s %-40s %4s %-6s %-13s %-60s";
    public static final String ROW_FORMAT = "%08d %-12s %-40s %-40s %4d %6.3f %13d %-60s";
    private static final Logger LOG = LogManager.getLogger();

    /**
     * Print the report.
     * 
     * @param out
     */
    public static void print(PrintStream out) {
        LOG.debug("Printing the Books Report");
        String text = null;
        out.println("\nBooks Report");
        out.println(HORIZONTAL_LINE);
        text = String.format(HEADER_FORMAT, "ID", "ISBN", "Authors", "Title", "Year", "Rating", "Ratings Count", "Image URL");
        out.println(text);
        out.println(HORIZONTAL_LINE);

        Collection<Book> books = AllData.getBooks().values();

        if (BookOptions.isByAuthorOptionSet()) {
            LOG.debug("isByAuthorOptionSet = true");
            List<Book> list = new ArrayList<>(books);
            if (BookOptions.isDescendingOptionSet()) {
                LOG.debug("isDescendingOptionSet = true");
                Collections.sort(list, new CompareByAuthorDescending());
            } else {
                LOG.debug("isDescendingOptionSet = false");
                Collections.sort(list, new CompareByAuthor());
            }

            books = list;
        }

        for (Book book : books) {
            long id = book.getId();
            String isbn = book.getIsbn();
            String authors = Common.truncateIfRequired(book.getAuthors(), 40);
            int year = book.getYear();
            String title = Common.truncateIfRequired(book.getTitle(), 40);
            float rating = book.getRating();
            int ratingsCount = book.getRatingsCount();
            String imageUrl = Common.truncateIfRequired(book.getImageUrl(), 60);
            text = String.format(ROW_FORMAT, id, isbn, authors, title, year, rating, ratingsCount, imageUrl);
            LOG.trace(text);
            out.println(text);
        }
    }

    public static class CompareByAuthor implements Comparator<Book> {
        @Override
        public int compare(Book book1, Book book2) {
            return book1.getAuthors().compareToIgnoreCase(book2.getAuthors());
        }
    }

    public static class CompareByAuthorDescending implements Comparator<Book> {
        @Override
        public int compare(Book book1, Book book2) {
            return -book1.getAuthors().compareToIgnoreCase(book2.getAuthors());
        }
    }

}
