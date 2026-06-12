import { useState, useCallback } from "react";
import type { ChatStats } from "@/lib/chat-parser";
import { chatService } from "@/services/chatService";

export function useChatAnalyzer() {
  const [stats, setStats] = useState<ChatStats | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const analyzeFile = useCallback(async (file: File) => {
    setIsLoading(true);
    setError(null);

    try {
      const parsedStats = await chatService.uploadChat(file);
      if (parsedStats.totalMessages === 0) {
        throw new Error("El archivo no contiene mensajes válidos");
        setStats(null);
      }
      setStats(parsedStats);
    } catch (err) {
      console.error(err);
      setError(
        "Error al procesar el archivo. Asegúrate de que el backend esté corriendo.",
      );
    } finally {
      setIsLoading(false);
    }
  }, []);

  const reset = useCallback(() => {
    setStats(null);
    setError(null);
  }, []);

  return { stats, isLoading, error, analyzeFile, reset };
}
