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

// Common Spanish stop words to filter out from word cloud
const STOP_WORDS = new Set([
  'de', 'la', 'que', 'el', 'en', 'y', 'a', 'los', 'del', 'se', 'las', 'por',
  'un', 'para', 'con', 'no', 'una', 'su', 'al', 'es', 'lo', 'como', 'más',
  'pero', 'sus', 'le', 'ya', 'o', 'fue', 'este', 'ha', 'sí', 'porque', 'esta',
  'son', 'entre', 'está', 'cuando', 'muy', 'sin', 'sobre', 'ser', 'tiene',
  'también', 'me', 'hasta', 'hay', 'donde', 'han', 'quien', 'están', 'estado',
  'desde', 'todo', 'nos', 'durante', 'estados', 'todos', 'uno', 'les', 'ni',
  'contra', 'otros', 'fueron', 'ese', 'eso', 'había', 'ante', 'ellos', 'e',
  'esto', 'mí', 'antes', 'algunos', 'qué', 'unos', 'yo', 'otro', 'otras',
  'otra', 'él', 'tanto', 'esa', 'estos', 'mucho', 'quienes', 'nada', 'muchos',
  'cual', 'sea', 'poco', 'ella', 'estar', 'haber', 'estas', 'estaba', 'estamos',
  'algunas', 'algo', 'nosotros', 'mi', 'mis', 'tú', 'te', 'ti', 'tu', 'tus',
  'ellas', 'nosotras', 'vosotros', 'vosotras', 'os', 'mío', 'mía', 'míos',
  'mías', 'tuyo', 'tuya', 'tuyos', 'tuyas', 'suyo', 'suya', 'suyos', 'suyas',
  'nuestro', 'nuestra', 'nuestros', 'nuestras', 'vuestro', 'vuestra', 'vuestros',
  'vuestras', 'esos', 'esas', 'estoy', 'estás', 'está', 'estamos', 'estáis',
  'están', 'como', 'así', 'pues', 'si', 'ver', 'hacer', 'puede', 'va', 'he',
  'the', 'and', 'to', 'of', 'is', 'in', 'it', 'you', 'that', 'was', 'for',
  'on', 'are', 'with', 'as', 'at', 'be', 'this', 'have', 'from', 'or', 'had',
  'by', 'not', 'but', 'what', 'all', 'were', 'we', 'when', 'your', 'can',
  'there', 'use', 'an', 'each', 'which', 'she', 'do', 'how', 'their', 'if',
  'will', 'up', 'other', 'about', 'out', 'many', 'then', 'them', 'these', 'so',
  'jaja', 'jajaja', 'jajajaja', 'jajajajaja', 'haha', 'hahaha', 'hahahaha',
  'xd', 'xdd', 'xddd', 'lol', 'omitido', 'multimedia', 'media', 'omitted',
]);

// Regex to match emojis
const EMOJI_REGEX = /[\u{1F600}-\u{1F64F}]|[\u{1F300}-\u{1F5FF}]|[\u{1F680}-\u{1F6FF}]|[\u{1F1E0}-\u{1F1FF}]|[\u{2600}-\u{26FF}]|[\u{2700}-\u{27BF}]|[\u{1F900}-\u{1F9FF}]|[\u{1FA00}-\u{1FA6F}]|[\u{1FA70}-\u{1FAFF}]/gu;

export function parseWhatsAppChat(content: string): ChatStats {
  const messages: Message[] = [];
  const participants = new Map<string, number>();
  const emojis = new Map<string, number>();
  const hourlyActivity = new Map<number, number>();
  const dailyActivity = new Map<string, number>();
  const wordFrequency = new Map<string, number>();

  // Initialize hourly activity
  for (let i = 0; i < 24; i++) {
    hourlyActivity.set(i, 0);
  }

  // WhatsApp export formats:
  // Format 1 (iOS): [DD/MM/YYYY, HH:MM:SS] Sender: Message
  // Format 2 (Android): DD/MM/YYYY, HH:MM - Sender: Message
  // Format 3 (Android): DD/MM/YY HH:MM - Sender: Message
  
  const patterns = [
    // iOS format: [DD/MM/YYYY, HH:MM:SS] or [DD/MM/YY, HH:MM:SS]
    /^\[(\d{1,2}\/\d{1,2}\/\d{2,4}),?\s*(\d{1,2}:\d{2}(?::\d{2})?)\s*(?:a\.?\s*m\.?|p\.?\s*m\.?)?\]\s*([^:]+):\s*(.+)$/i,
    // Android format: DD/MM/YYYY, HH:MM - or DD/MM/YY, HH:MM -
    /^(\d{1,2}\/\d{1,2}\/\d{2,4}),?\s*(\d{1,2}:\d{2}(?::\d{2})?)\s*(?:a\.?\s*m\.?|p\.?\s*m\.?)?\s*[-–]\s*([^:]+):\s*(.+)$/i,
    // Alternative format with different separators
    /^(\d{1,2}[-./]\d{1,2}[-./]\d{2,4}),?\s*(\d{1,2}:\d{2}(?::\d{2})?)\s*(?:a\.?\s*m\.?|p\.?\s*m\.?)?\s*[-–]\s*([^:]+):\s*(.+)$/i,
  ];

  const lines = content.split('\n');
  let currentMessage: Message | null = null;

  for (const line of lines) {
    let match = null;
    
    for (const pattern of patterns) {
      match = line.match(pattern);
      if (match) break;
    }

    if (match) {
      const [, dateStr, timeStr, sender, messageContent] = match;
      
      // Parse date
      const dateParts = dateStr.split(/[-./]/);
      let day = parseInt(dateParts[0]);
      let month = parseInt(dateParts[1]) - 1;
      let year = parseInt(dateParts[2]);
      
      // Handle 2-digit year
      if (year < 100) {
        year += year > 50 ? 1900 : 2000;
      }
      
      const date = new Date(year, month, day);
      
      // Parse hour
      const timeParts = timeStr.split(':');
      let hour = parseInt(timeParts[0]);
      
      // Handle AM/PM if present in original line
      if (/p\.?\s*m\.?/i.test(line) && hour < 12) {
        hour += 12;
      } else if (/a\.?\s*m\.?/i.test(line) && hour === 12) {
        hour = 0;
      }

      const cleanSender = sender.trim();
      
      // Skip system messages
      if (
        cleanSender.includes('cifrado') ||
        cleanSender.includes('encrypted') ||
        cleanSender.includes('creó') ||
        cleanSender.includes('created') ||
        cleanSender.includes('añadió') ||
        cleanSender.includes('added') ||
        cleanSender.includes('cambió') ||
        cleanSender.includes('changed')
      ) {
        continue;
      }

      currentMessage = {
        date,
        time: timeStr,
        sender: cleanSender,
        content: messageContent.trim(),
        hour,
        dayOfWeek: date.getDay(),
      };

      messages.push(currentMessage);

      // Count messages per participant
      participants.set(cleanSender, (participants.get(cleanSender) || 0) + 1);

      // Count hourly activity
      hourlyActivity.set(hour, (hourlyActivity.get(hour) || 0) + 1);

      // Count daily activity
      const dateKey = date.toISOString().split('T')[0];
      dailyActivity.set(dateKey, (dailyActivity.get(dateKey) || 0) + 1);

      // Extract emojis
      const messageEmojis = messageContent.match(EMOJI_REGEX);
      if (messageEmojis) {
        for (const emoji of messageEmojis) {
          emojis.set(emoji, (emojis.get(emoji) || 0) + 1);
        }
      }

      // Extract words
      const words = messageContent
        .toLowerCase()
        .replace(EMOJI_REGEX, '')
        .replace(/[^\p{L}\p{N}\s]/gu, '')
        .split(/\s+/)
        .filter(word => 
          word.length > 2 && 
          !STOP_WORDS.has(word) &&
          !/^\d+$/.test(word)
        );

      for (const word of words) {
        wordFrequency.set(word, (wordFrequency.get(word) || 0) + 1);
      }
    } else if (currentMessage && line.trim()) {
      // Multi-line message continuation
      currentMessage.content += '\n' + line.trim();
      
      // Also process emojis and words from continuation
      const messageEmojis = line.match(EMOJI_REGEX);
      if (messageEmojis) {
        for (const emoji of messageEmojis) {
          emojis.set(emoji, (emojis.get(emoji) || 0) + 1);
        }
      }
    }
  }

  // Calculate top sender
  let topSender = { name: '', count: 0 };
  for (const [name, count] of participants) {
    if (count > topSender.count) {
      topSender = { name, count };
    }
  }

  // Calculate top emoji
  let topEmoji = { emoji: '', count: 0 };
  for (const [emoji, count] of emojis) {
    if (count > topEmoji.count) {
      topEmoji = { emoji, count };
    }
  }

  // Calculate peak hour
  let peakHour = { hour: 0, count: 0 };
  for (const [hour, count] of hourlyActivity) {
    if (count > peakHour.count) {
      peakHour = { hour, count };
    }
  }

  // Calculate busiest days (top 10)
  const busiestDays = Array.from(dailyActivity.entries())
    .sort((a, b) => b[1] - a[1])
    .slice(0, 10)
    .map(([date, count]) => ({ date, count }));

  // Calculate top words (top 100 for word cloud)
  const topWords = Array.from(wordFrequency.entries())
    .sort((a, b) => b[1] - a[1])
    .slice(0, 100)
    .map(([word, count]) => ({ word, count }));

  return {
    totalMessages: messages.length,
    participants,
    topSender,
    emojis,
    topEmoji,
    hourlyActivity,
    peakHour,
    dailyActivity,
    busiestDays,
    wordFrequency,
    topWords,
    messages,
  };
}

export function formatHour(hour: number): string {
  const period = hour >= 12 ? 'PM' : 'AM';
  const displayHour = hour % 12 || 12;
  return `${displayHour}:00 ${period}`;
}

export function formatDate(dateStr: string): string {
  const date = new Date(dateStr);
  return date.toLocaleDateString('es-ES', {
    weekday: 'short',
    day: 'numeric',
    month: 'short',
    year: 'numeric',
  });
}

export function getDayName(day: number): string {
  const days = ['Dom', 'Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb'];
  return days[day];
}
