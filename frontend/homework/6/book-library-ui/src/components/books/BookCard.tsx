import type { Book } from "../../lib/types";

interface BookCardProps {
  book: Book;
}

export default function BookCard({ book }: BookCardProps) {
  const cardClass = book.available
    ? "bookCard bookCard--available"
    : "bookCard bookCard--unavailable";

  return (
    <div className={cardClass}>
      <div className="bookCardHeader">
        <h3 className="bookTitle">{book.title}</h3>

        <span className="badge">
          {book.available ? "Available" : "Unavailable"}
        </span>
      </div>

      <div className="meta">
        <p>
          <strong>Author:</strong> {book.author}
        </p>
        <p>
          <strong>Genre:</strong> {book.genre}
        </p>
        <p>
          <strong>Rating:</strong> {book.rating} / 5
        </p>
      </div>

      {book.description && (
        <p className="desc">{book.description}</p>
      )}
    </div>
  );
}
