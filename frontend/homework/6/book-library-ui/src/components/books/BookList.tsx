import type { Book } from "../../lib/types";
import BookCard from "./BookCard";

interface BookListProps {
  readonly books: readonly Book[];
}

export default function BookList({ books }: BookListProps) {
  if (books.length === 0) {
    return <p>No books match your filters.</p>;
  }

  return (
    <div className="libraryGrid">
      {books.map((book) => (
        <BookCard key={book.id} book={book} />
      ))}
    </div>
  );
}
