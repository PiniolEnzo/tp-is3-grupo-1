"use client";

import { useCallback, useState } from "react";
import { Upload, FileText, X } from "lucide-react";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";

interface FileUploadProps {
  onFileLoaded: (file: File) => void;
  isLoading?: boolean;
}

export function FileUpload({ onFileLoaded, isLoading }: FileUploadProps) {
  const [isDragging, setIsDragging] = useState(false);
  const [fileName, setFileName] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const processFile = useCallback(
    (file: File) => {
      setError(null);

      if (!file.name.endsWith(".txt")) {
        setError("Por favor, sube un archivo .txt exportado de WhatsApp");
        return;
      }

      setFileName(file.name);
      onFileLoaded(file);
    },
    [onFileLoaded],
  );

  const handleDragOver = useCallback((e: React.DragEvent) => {
    e.preventDefault();
    setIsDragging(true);
  }, []);

  const handleDragLeave = useCallback((e: React.DragEvent) => {
    e.preventDefault();
    setIsDragging(false);
  }, []);

  const handleDrop = useCallback(
    (e: React.DragEvent) => {
      e.preventDefault();
      setIsDragging(false);

      const file = e.dataTransfer.files[0];
      if (file) {
        processFile(file);
      }
    },
    [processFile],
  );

  const handleFileInput = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      const file = e.target.files?.[0];
      if (file) {
        processFile(file);
      }
    },
    [processFile],
  );

  const handleReset = useCallback(() => {
    setFileName(null);
    setError(null);
  }, []);

  return (
    <div className="w-full max-w-2xl mx-auto">
      <div
        onDragOver={handleDragOver}
        onDragLeave={handleDragLeave}
        onDrop={handleDrop}
        className={cn(
          "relative border-2 border-dashed rounded-xl p-8 md:p-12 transition-all duration-200",
          "flex flex-col items-center justify-center gap-4 text-center",
          isDragging
            ? "border-primary bg-primary/5"
            : "border-border hover:border-primary/50 hover:bg-secondary/50",
          isLoading && "pointer-events-none opacity-50",
        )}
      >
        {fileName ? (
          <>
            <div className="w-16 h-16 rounded-full bg-primary/10 flex items-center justify-center">
              <FileText className="w-8 h-8 text-primary" />
            </div>
            <div className="flex flex-col items-center gap-2">
              <p className="text-foreground font-medium">{fileName}</p>
              <Button
                variant="ghost"
                size="sm"
                onClick={handleReset}
                className="text-muted-foreground hover:text-foreground"
              >
                <X className="w-4 h-4 mr-1" />
                Cambiar archivo
              </Button>
            </div>
          </>
        ) : (
          <>
            <div className="w-16 h-16 rounded-full bg-secondary flex items-center justify-center">
              <Upload className="w-8 h-8 text-muted-foreground" />
            </div>
            <div className="flex flex-col items-center gap-2">
              <p className="text-foreground font-medium">
                Arrastra tu archivo de chat aquí
              </p>
              <p className="text-muted-foreground text-sm">
                o haz clic para seleccionar
              </p>
            </div>
            <input
              type="file"
              accept=".txt"
              onChange={handleFileInput}
              className="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
            />
          </>
        )}
      </div>

      {error && (
        <p className="text-destructive text-sm mt-3 text-center">{error}</p>
      )}

      <div className="mt-6 p-4 bg-secondary/50 rounded-lg">
        <p className="text-sm text-muted-foreground">
          <strong className="text-foreground">¿Cómo exportar tu chat?</strong>
          <br />
          Abre WhatsApp → Chat → Menú (⋮) → Más → Exportar chat → Sin archivos
        </p>
      </div>
    </div>
  );
}
