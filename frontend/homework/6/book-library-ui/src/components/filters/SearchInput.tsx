interface SearchInputProps {
  value: string;
  onChange: (value: string) => void;
}

export default function SearchInput({ value, onChange }: SearchInputProps) {
  return (
    <div>
      <label htmlFor="search-input" className="label">
        Search (Title / Author)
      </label>

      <input
        id="search-input"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder="e.g., Jane"
        className="input"
      />
    </div>
  );
}
