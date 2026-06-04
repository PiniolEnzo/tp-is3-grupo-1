"use client";

import { Card } from "@/components/ui/card";
import { formatHour } from "@/lib/chat-parser";
import {
  AreaChart,
  Area,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  CartesianGrid,
} from "recharts";

interface HourlyChartProps {
  data: Map<number, number>;
}

export function HourlyChart({ data }: HourlyChartProps) {
  const chartData = Array.from({ length: 24 }, (_, hour) => ({
    hour,
    label: formatHour(hour),
    messages: data.get(hour) || 0,
  }));

  const CustomTooltip = ({ active, payload, label }: any) => {
    if (active && payload && payload.length) {
      return (
        <div className="bg-popover border border-border rounded-lg p-3 shadow-lg">
          <p className="text-sm font-medium text-foreground">
            {formatHour(label)}
          </p>
          <p className="text-sm text-primary">
            {payload[0].value} mensajes
          </p>
        </div>
      );
    }
    return null;
  };

  return (
    <Card className="p-6 bg-card border-border">
      <h3 className="text-lg font-semibold text-foreground mb-4">
        Actividad por Hora
      </h3>
      <div className="h-[300px] w-full">
        <ResponsiveContainer width="100%" height="100%">
          <AreaChart data={chartData} margin={{ top: 10, right: 10, left: -10, bottom: 0 }}>
            <defs>
              <linearGradient id="colorMessages" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor="oklch(0.72 0.19 145)" stopOpacity={0.4} />
                <stop offset="95%" stopColor="oklch(0.72 0.19 145)" stopOpacity={0} />
              </linearGradient>
            </defs>
            <CartesianGrid strokeDasharray="3 3" stroke="oklch(0.30 0.01 260)" />
            <XAxis
              dataKey="hour"
              tickFormatter={(hour) => `${hour}h`}
              stroke="oklch(0.65 0 0)"
              fontSize={12}
              tickLine={false}
              axisLine={false}
            />
            <YAxis
              stroke="oklch(0.65 0 0)"
              fontSize={12}
              tickLine={false}
              axisLine={false}
            />
            <Tooltip content={<CustomTooltip />} />
            <Area
              type="monotone"
              dataKey="messages"
              stroke="oklch(0.72 0.19 145)"
              strokeWidth={2}
              fill="url(#colorMessages)"
            />
          </AreaChart>
        </ResponsiveContainer>
      </div>
    </Card>
  );
}
