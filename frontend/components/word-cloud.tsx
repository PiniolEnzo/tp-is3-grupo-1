"use client";

import { useMemo } from "react";
import { Card } from "@/components/ui/card";

interface WordCloudProps {
  words: { word: string; count: number }[];
}

export function WordCloud({ words }: WordCloudProps) {
  const processedWords = useMemo(() => {
    if (words.length === 0) return [];

    const maxCount = Math.max(...words.map((w) => w.count));
    const minCount = Math.min(...words.map((w) => w.count));
    const range = maxCount - minCount || 1;

    // Color palette for words
    const colors = [
      "text-chart-1",
      "text-chart-2",
      "text-chart-3",
      "text-chart-4",
      "text-chart-5",
      "text-primary",
      "text-foreground",
    ];

    return words.slice(0, 60).map((word, index) => {
      const normalized = (word.count - minCount) / range;
      // Font size from 0.75rem to 2.5rem based on frequency
      const fontSize = 0.75 + normalized * 1.75;
      const opacity = 0.5 + normalized * 0.5;
      const colorIndex = Math.floor(Math.random() * colors.length);

      return {
        ...word,
        fontSize,
        opacity,
        color: colors[colorIndex],
        rotation: Math.random() > 0.7 ? (Math.random() > 0.5 ? -5 : 5) : 0,
      };
    });
  }, [words]);

  // Shuffle words for more natural look
  const shuffledWords = useMemo(() => {
    return [...processedWords].sort(() => Math.random() - 0.5);
  }, [processedWords]);

  if (words.length === 0) {
    return (
      <Card className="p-6 bg-card border-border">
        <h3 className="text-lg font-semibold text-foreground mb-4">
          Nube de Palabras
        </h3>
        <div className="h-64 flex items-center justify-center text-muted-foreground">
          No hay datos suficientes
        </div>
      </Card>
    );
  }

  return (
    <Card className="p-6 bg-card border-border">
      <h3 className="text-lg font-semibold text-foreground mb-4">
        Nube de Palabras
      </h3>
      <div className="flex flex-wrap items-center justify-center gap-2 md:gap-3 min-h-[300px] py-4">
        {shuffledWords.map((word, index) => (
          <span
            key={`${word.word}-${index}`}
            className={`${word.color} font-medium transition-transform hover:scale-110 cursor-default`}
            style={{
              fontSize: `${word.fontSize}rem`,
              opacity: word.opacity,
              transform: `rotate(${word.rotation}deg)`,
            }}
            title={`${word.word}: ${word.count} veces`}
          >
            {word.word}
          </span>
        ))}
      </div>
    </Card>
  );
}
