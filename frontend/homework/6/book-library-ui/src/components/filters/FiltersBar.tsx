import type { GenreFilter } from "../../lib/library.service";
import SearchInput from "./SearchInput";
import GenreSelect from "./GenreSelect";
import RatingSlider from "./RatingSlider";

interface FiltersBarProps {
  query: string;
  onQueryChange: (value: string) => void;

  genre: GenreFilter;
  onGenreChange: (value: GenreFilter) => void;

  minRating: number;
  onMinRatingChange: (value: number) => void;

  genreOptions: GenreFilter[];
}

export default function FiltersBar({
  query,
  onQueryChange,
  genre,
  onGenreChange,
  minRating,
  onMinRatingChange,
  genreOptions
}: FiltersBarProps) {
  return (
    <div className="filtersBar">
      <SearchInput value={query} onChange={onQueryChange} />

      <GenreSelect
        value={genre}
        options={genreOptions}
        onChange={onGenreChange}
      />

      <RatingSlider
        value={minRating}
        onChange={onMinRatingChange}
      />
    </div>
  );
}
