"use client";

import { Card } from "@/components/ui/card";

interface EmojiChartProps {
  data: Map<string, number>;
}

export function EmojiChart({ data }: EmojiChartProps) {
  const topEmojis = Array.from(data.entries())
    .sort((a, b) => b[1] - a[1])
    .slice(0, 10);

  const maxCount = topEmojis[0]?.[1] || 1;

  if (topEmojis.length === 0) {
    return (
      <Card className="p-6 bg-card border-border">
        <h3 className="text-lg font-semibold text-foreground mb-4">
          Emojis Más Usados
        </h3>
        <div className="h-64 flex items-center justify-center text-muted-foreground">
          No se encontraron emojis
        </div>
      </Card>
    );
  }

  return (
    <Card className="p-6 bg-card border-border">
      <h3 className="text-lg font-semibold text-foreground mb-4">
        Emojis Más Usados
      </h3>
      <div className="space-y-3">
        {topEmojis.map(([emoji, count], index) => (
          <div key={`${emoji}-${index}`} className="flex items-center gap-3">
            <span className="text-2xl w-8 text-center">{emoji}</span>
            <div className="flex-1">
              <div className="flex items-center justify-between mb-1">
                <span className="text-sm text-muted-foreground">
                  #{index + 1}
                </span>
                <span className="text-sm font-medium text-foreground">
                  {count}
                </span>
              </div>
              <div className="h-2 bg-secondary rounded-full overflow-hidden">
                <div
                  className="h-full bg-chart-3 rounded-full transition-all duration-500"
                  style={{ width: `${(count / maxCount) * 100}%` }}
                />
              </div>
            </div>
          </div>
        ))}
      </div>
    </Card>
  );
}
