import type { ChatStats } from "@/lib/chat-parser";

const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

export const chatService = {
  async uploadChat(file: File): Promise<ChatStats> {
    const formData = new FormData();
    formData.append("file", file);

    const response = await fetch(`${API_URL}/api/chat/upload`, {
      method: "POST",
      body: formData,
    });

    if (!response.ok) {
      throw new Error("Error al procesar el archivo en el servidor");
    }

    const data = await response.json();
    const totalMessagesSum = Object.values(
      data.mensajesPorUsuario || {},
    ).reduce((a: any, b: any) => a + b, 0) as number;

    const parsedStats: ChatStats = {
      totalMessages: totalMessagesSum,
      participants: new Map(Object.entries(data.mensajesPorUsuario || {})),
      topSender: {
        name: data.usuarioConMasMensajes,
        count: data.cantidadMensajesUsuarioMasActivo || 0,
      },
      dailyActivity: new Map(Object.entries(data.mensajesPorDia || {})),
      hourlyActivity: new Map(
        Array.from(
          { length: 24 },
          (_, i) =>
            [
              i,
              data.mensajesPorHora
                ? data.mensajesPorHora[i.toString()] || 0
                : 0,
            ] as [number, number],
        ),
      ),
      topWords: Object.entries(data.frecuenciaPalabras || {}).map(
        ([word, count]) => ({
          word,
          count: count as number,
        }),
      ),
      emojis: new Map(Object.entries(data.frecuenciaEmojis || {})),
      topEmoji: {
        emoji: data.emojiMasUtilizado,
        count: data.frecuenciaEmojis
          ? data.frecuenciaEmojis[data.emojiMasUtilizado] || 0
          : 0,
      },
      wordFrequency: new Map(Object.entries(data.frecuenciaPalabras || {})),
      peakHour: {
        hour: data.horaMasActiva || 0,
        count: data.mensajesPorHora
          ? data.mensajesPorHora[(data.horaMasActiva || 0).toString()] || 0
          : 0,
      },
      busiestDays: Object.entries(data.mensajesPorDia || {})
        .map(([date, count]) => ({
          date,
          count: count as number,
        }))
        .sort((a, b) => {
          const parseDate = (d: string) => {
            const parts = d.split(/[-/]/);
            if (parts.length === 3) {
              // Si es formato DD/MM/YYYY o DD-MM-YYYY
              if (parts[2].length === 4) {
                return new Date(
                  `${parts[2]}-${parts[1].padStart(2, "0")}-${parts[0].padStart(2, "0")}`,
                ).getTime();
              }
              // Si es YYYY-MM-DD
              if (parts[0].length === 4) {
                return new Date(
                  `${parts[0]}-${parts[1].padStart(2, "0")}-${parts[2].padStart(2, "0")}`,
                ).getTime();
              }
            }
            return new Date(d).getTime();
          };
          return parseDate(a.date) - parseDate(b.date);
        }),
      messages: [],
    };
    return parsedStats;
  },

  async testConnection(): Promise<string> {
    const response = await fetch(`${API_URL}/api/test`);
    if (!response.ok) {
      throw new Error("No se pudo conectar con el backend");
    }
    return response.text();
  },
};
