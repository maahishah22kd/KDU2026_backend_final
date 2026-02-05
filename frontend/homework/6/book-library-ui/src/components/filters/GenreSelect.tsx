import type { GenreFilter } from "../../lib/library.service";

interface GenreSelectProps {
  value: GenreFilter;
  options: GenreFilter[];
  onChange: (value: GenreFilter) => void;
}

export default function GenreSelect({
  value,
  options,
  onChange
}: GenreSelectProps) {
  return (
    <div>
      <label htmlFor="genre-select" className="label">
        Genre
      </label>

      <select
        id="genre-select"
        value={value}
        onChange={(e) => onChange(e.target.value as GenreFilter)}
        className="select"
      >
        {options.map((g) => (
          <option key={g} value={g}>
            {g}
          </option>
        ))}
      </select>
    </div>
  );
}
