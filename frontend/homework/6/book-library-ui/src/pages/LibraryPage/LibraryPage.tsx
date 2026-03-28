import { useMemo, useState } from "react";
import FiltersBar from "../../components/filters/FiltersBar";
import BookList from "../../components/books/BookList";
import StatsPanel from "../../components/stats/StatsPanel";
import {
  applyFilters,
  computeStats,
  fetchBooksFromData,
  getGenreOptions,
  type GenreFilter
} from "../../lib/library.service";

export default function LibraryPage() {
  const allBooks = useMemo(() => fetchBooksFromData(), []);

  const [query, setQuery] = useState("");
  const [genre, setGenre] = useState<GenreFilter>("All");
  const [minRating, setMinRating] = useState(0);

  const genreOptions = useMemo(() => getGenreOptions(), []);

  const filteredBooks = useMemo(
    () => applyFilters(allBooks, { query, genre, minRating }),
    [allBooks, query, genre, minRating]
  );

  const stats = useMemo(() => computeStats(filteredBooks), [filteredBooks]);

  return (
    <div className="page">
      <h1 className="pageTitle">Book Library Manager</h1>
      <p className="subheading">Search and filter your local book collection.</p>

      <FiltersBar
        query={query}
        onQueryChange={setQuery}
        genre={genre}
        onGenreChange={setGenre}
        minRating={minRating}
        onMinRatingChange={setMinRating}
        genreOptions={genreOptions}
      />

      <StatsPanel stats={stats} />

      <BookList books={filteredBooks} />
    </div>
  );
}
