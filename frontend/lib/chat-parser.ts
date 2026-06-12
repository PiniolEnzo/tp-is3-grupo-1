export interface Message {
  date: Date;
  time: string;
  sender: string;
  content: string;
  hour: number;
  dayOfWeek: number;
}

export interface ChatStats {
  totalMessages: number;
  participants: Map<string, number>;
  topSender: { name: string; count: number };
  emojis: Map<string, number>;
  topEmoji: { emoji: string; count: number };
  hourlyActivity: Map<number, number>;
  peakHour: { hour: number; count: number };
  dailyActivity: Map<string, number>;
  busiestDays: { date: string; count: number }[];
  wordFrequency: Map<string, number>;
  topWords: { word: string; count: number }[];
  messages: Message[];
}

export function formatHour(hour: number): string {
  const period = hour >= 12 ? "PM" : "AM";
  const displayHour = hour % 12 || 12;
  return `${displayHour}:00 ${period}`;
}

export function formatDate(dateStr: string): string {
  try {
    const parts = dateStr.split(/[-/]/);
    let date: Date;

    if (parts.length === 3) {
      if (parts[2].length === 4) {
        // Formato: DD/MM/YYYY
        date = new Date(
          `${parts[2]}-${parts[1].padStart(2, "0")}-${parts[0].padStart(2, "0")}T12:00:00`,
        );
      } else if (parts[0].length === 4) {
        // Formato: YYYY-MM-DD
        date = new Date(
          `${parts[0]}-${parts[1].padStart(2, "0")}-${parts[2].padStart(2, "0")}T12:00:00`,
        );
      } else {
        date = new Date(dateStr);
      }
    } else {
      date = new Date(dateStr);
    }

    if (isNaN(date.getTime())) {
      return dateStr;
    }

    return date.toLocaleDateString("es-ES", {
      weekday: "short",
      day: "numeric",
      month: "short",
      year: "numeric",
    });
  } catch (e) {
    return dateStr;
  }
}

export function getDayName(day: number): string {
  const days = ["Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"];
  return days[day];
}
