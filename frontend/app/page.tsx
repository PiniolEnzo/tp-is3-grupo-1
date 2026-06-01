"use client";

import { useState, useCallback, useEffect } from "react";
import { MessageCircle } from "lucide-react";
import { FileUpload } from "@/components/file-upload";
import { Dashboard } from "@/components/dashboard";
import { parseWhatsAppChat, type ChatStats } from "@/lib/chat-parser";
import { Spinner } from "@/components/ui/spinner";

export default function Home() {
  const [stats, setStats] = useState<ChatStats | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [backendStatus, setBackendStatus] = useState("");

  useEffect(() => {
    fetch("http://localhost:8080/api/test")
      .then((res) => res.text())
      .then((data) => setBackendStatus(data))
      .catch(() => setBackendStatus("No se pudo conectar con el backend"));
  }, []);

  const handleFileLoaded = useCallback((content: string) => {
    setIsLoading(true);
    setError(null);

    setTimeout(() => {
      try {
        const parsedStats = parseWhatsAppChat(content);

        if (parsedStats.totalMessages === 0) {
          setError(
            "No se encontraron mensajes en el archivo. Asegúrate de que sea un chat de WhatsApp exportado correctamente."
          );
          setIsLoading(false);
          return;
        }

        setStats(parsedStats);
      } catch {
        setError(
          "Error al procesar el archivo. Por favor, verifica el formato."
        );
      } finally {
        setIsLoading(false);
      }
    }, 100);
  }, []);

  const handleReset = useCallback(() => {
    setStats(null);
    setError(null);
  }, []);

  if (stats) {
    return <Dashboard stats={stats} onReset={handleReset} />;
  }

  return (
    <div className="min-h-screen bg-background flex flex-col">
      <header className="border-b border-border bg-card/50 backdrop-blur-sm">
        <div className="container mx-auto px-4 py-6">
          <div className="flex flex-col items-center justify-center gap-3">
            <div className="flex items-center justify-center gap-3">
              <div className="w-10 h-10 rounded-lg bg-primary/10 flex items-center justify-center">
                <MessageCircle className="w-5 h-5 text-primary" />
              </div>

              <h1 className="text-2xl md:text-3xl font-bold text-foreground">
                WhatsApp Chat Analyzer
              </h1>
            </div>

            <p className="text-sm text-muted-foreground">
              Estado backend: {backendStatus}
            </p>
          </div>
        </div>
      </header>

      <main className="flex-1 flex flex-col items-center justify-center px-4 py-12">
        <div className="w-full max-w-2xl text-center mb-8">
          <h2 className="text-xl md:text-2xl font-semibold text-foreground mb-3">
            Analiza tus conversaciones de WhatsApp
          </h2>

          <p className="text-muted-foreground">
            Descubre estadísticas interesantes sobre tus chats grupales: quién
            escribe más, los emojis favoritos, las horas de mayor actividad y
            mucho más.
          </p>
        </div>

        {isLoading ? (
          <div className="flex flex-col items-center gap-4 py-12">
            <Spinner className="w-8 h-8 text-primary" />
            <p className="text-muted-foreground">Analizando tu chat...</p>
          </div>
        ) : (
          <FileUpload onFileLoaded={handleFileLoaded} isLoading={isLoading} />
        )}

        {error && (
          <div className="mt-6 p-4 bg-destructive/10 border border-destructive/20 rounded-lg max-w-2xl">
            <p className="text-destructive text-sm">{error}</p>
          </div>
        )}

        <div className="mt-16 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 max-w-4xl w-full">
          {[
            {
              title: "Usuario más activo",
              description: "Descubre quién envía más mensajes en el grupo",
            },
            {
              title: "Emojis favoritos",
              description: "Los emojis más utilizados en la conversación",
            },
            {
              title: "Hora pico",
              description: "La franja horaria con mayor actividad",
            },
            {
              title: "Nube de palabras",
              description: "Las palabras más frecuentes en el chat",
            },
          ].map((feature, index) => (
            <div
              key={index}
              className="p-4 bg-card border border-border rounded-lg"
            >
              <h3 className="font-medium text-foreground mb-1">
                {feature.title}
              </h3>

              <p className="text-sm text-muted-foreground">
                {feature.description}
              </p>
            </div>
          ))}
        </div>
      </main>

      <footer className="border-t border-border py-6">
        <div className="container mx-auto px-4 text-center">
          <p className="text-sm text-muted-foreground">
            Trabajo Práctico: Análisis Estadístico de Chats de WhatsApp
          </p>

          <p className="text-xs text-muted-foreground mt-1">
            Tus datos se procesan localmente y no se envían a ningún servidor.
          </p>
        </div>
      </footer>
    </div>
  );
}