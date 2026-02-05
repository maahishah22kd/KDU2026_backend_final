import type { LibraryStats } from "../../lib/library.service";

interface StatsPanelProps {
  stats: LibraryStats;
}

export default function StatsPanel({ stats }: StatsPanelProps) {
  return (
    <div className="statsCard">
      <h2 className="statsTitle">Statistics</h2>

      <div className="statsRow">
        <div>
          <strong>Total:</strong> {stats.total}
        </div>
        <div>
          <strong>Available:</strong> {stats.available}
        </div>
        <div>
          <strong>Unavailable:</strong> {stats.unavailable}
        </div>
        <div>
          <strong>Avg Rating:</strong> {stats.avgRating}
        </div>
      </div>
    </div>
  );
}
