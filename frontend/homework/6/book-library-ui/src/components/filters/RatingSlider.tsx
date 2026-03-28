interface RatingSliderProps {
  readonly value: number;
  readonly onChange: (value: number) => void;
}

export default function RatingSlider({ value, onChange }: RatingSliderProps) {
  return (
    <div>
      <label className="label">
        Min Rating: {value.toFixed(1)}
      </label>

      <input
        type="range"
        min={0}
        max={5}
        step={0.1}
        value={value}
        onChange={(e) => onChange(Number(e.target.value))}
        className="range"
      />
    </div>
  );
}
