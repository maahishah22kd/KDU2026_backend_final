import { BOOKS_DATA } from "../data/books.data";
import type { Book, Genre } from "./types";

export type GenreFilter = "All" | Genre;

export interface LibraryFilters {
  query: string;
  genre: GenreFilter;
  minRating: number; 
}

export interface LibraryStats {
  total: number;
  available: number;
  unavailable: number;
  avgRating: number;
}

export function fetchBooksFromData(): Book[] {
  return [...BOOKS_DATA];
}

export function searchBooks(query: string, books: ReadonlyArray<Book>): Book[] {
  const q = query.trim().toLowerCase();
  if (!q) return [...books];

  const results = books.filter((book) => {
    const title = book.title.toLowerCase();
    const author = book.author.toLowerCase();
    return title.includes(q) || author.includes(q);
  });

  console.log(results);
  return results;
}

export function getBooksByGenre(genre: Genre, books: ReadonlyArray<Book>): Book[]{
  return books.filter((b) => b.genre === genre);
}

export function getBooksByMinRating(minRating: number, books: ReadonlyArray<Book>): Book[]{
  return books.filter((b) => b.rating >= minRating);
}

export function applyFilters(books: ReadonlyArray<Book>, filters: LibraryFilters): Book[]{
  let current = [...books];
  current = searchBooks(filters.query, current);

  if (filters.genre !== "All") {
    current = getBooksByGenre(filters.genre, current);
  }

  if (filters.minRating > 0) {
    current = getBooksByMinRating(filters.minRating, current);
  }

  return current;
}

export function computeStats(books: ReadonlyArray<Book>): LibraryStats {
  const total = books.length;
  const available = books.filter((b) => b.available).length;
  const unavailable = total - available;

  const avgRating =
    total === 0
      ? 0
      : Math.round((books.reduce((sum, b) => sum + b.rating, 0) / total) * 10) / 10;

  return { total, available, unavailable, avgRating };
}

export function getGenreOptions(): GenreFilter[] {
  return ["All", "Comic", "Horror", "Romance"];
}
