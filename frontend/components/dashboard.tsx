"use client";

import { useMemo } from "react";
import { MessageSquare, User, Clock, Calendar, Smile } from "lucide-react";
import { StatCard } from "@/components/stat-card";
import { HourlyChart } from "@/components/hourly-chart";
import { DailyChart } from "@/components/daily-chart";
import { ParticipantsChart } from "@/components/participants-chart";
import { EmojiChart } from "@/components/emoji-chart";
import { WordCloud } from "@/components/word-cloud";
import { Button } from "@/components/ui/button";
import type { ChatStats } from "@/lib/chat-parser";
import { formatHour } from "@/lib/chat-parser";

interface DashboardProps {
  stats: ChatStats;
  onReset: () => void;
}

export function Dashboard({ stats, onReset }: DashboardProps) {
  const participantCount = useMemo(
    () => stats.participants.size,
    [stats.participants]
  );

  const dateRange = useMemo(() => {
    if (stats.messages.length === 0) return "";
    const dates = stats.messages.map((m) => m.date.getTime());
    const minDate = new Date(Math.min(...dates));
    const maxDate = new Date(Math.max(...dates));
    return `${minDate.toLocaleDateString("es-ES")} - ${maxDate.toLocaleDateString("es-ES")}`;
  }, [stats.messages]);

  return (
    <div className="min-h-screen bg-background">
      {/* Header */}
      <header className="border-b border-border bg-card/50 backdrop-blur-sm sticky top-0 z-10">
        <div className="container mx-auto px-4 py-4 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
          <div>
            <h1 className="text-xl md:text-2xl font-bold text-foreground">
              WhatsApp Chat Analyzer
            </h1>
            <p className="text-sm text-muted-foreground">{dateRange}</p>
          </div>
          <Button variant="outline" onClick={onReset}>
            Analizar otro chat
          </Button>
        </div>
      </header>

      {/* Main Content */}
      <main className="container mx-auto px-4 py-6 md:py-8">
        {/* Stats Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-4 mb-8">
          <StatCard
            title="Total de Mensajes"
            value={stats.totalMessages.toLocaleString()}
            icon={MessageSquare}
          />
          <StatCard
            title="Usuario Más Activo"
            value={stats.topSender.name || "N/A"}
            subtitle={`${stats.topSender.count.toLocaleString()} mensajes`}
            icon={User}
          />
          <StatCard
            title="Hora Pico"
            value={formatHour(stats.peakHour.hour)}
            subtitle={`${stats.peakHour.count.toLocaleString()} mensajes`}
            icon={Clock}
          />
          <StatCard
            title="Emoji Favorito"
            value={stats.topEmoji.emoji || "N/A"}
            subtitle={`${stats.topEmoji.count.toLocaleString()} veces`}
            icon={Smile}
          />
          <StatCard
            title="Participantes"
            value={participantCount}
            icon={Calendar}
          />
        </div>

        {/* Charts Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">
          <HourlyChart data={stats.hourlyActivity} />
          <DailyChart data={stats.busiestDays} />
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">
          <ParticipantsChart data={stats.participants} />
          <EmojiChart data={stats.emojis} />
        </div>

        {/* Word Cloud */}
        <WordCloud words={stats.topWords} />

        {/* Footer */}
        <footer className="mt-12 pt-6 border-t border-border text-center">
          <p className="text-sm text-muted-foreground">
            Trabajo Práctico: Análisis Estadístico de Chats de WhatsApp
          </p>
        </footer>
      </main>
    </div>
  );
}
