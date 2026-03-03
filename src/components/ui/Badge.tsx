import type { ReactNode } from 'react';

type BadgeVariant = 'default' | 'ok' | 'upcoming' | 'warning' | 'critical' | 'overdue';

interface BadgeProps {
  variant?: BadgeVariant;
  children: ReactNode;
  className?: string;
}

const variantClasses: Record<BadgeVariant, string> = {
  default: 'bg-slate-700 text-slate-300',
  ok: 'bg-green-900/50 text-green-400',
  upcoming: 'bg-blue-900/50 text-blue-400',
  warning: 'bg-yellow-900/50 text-yellow-400',
  critical: 'bg-orange-900/50 text-orange-400',
  overdue: 'bg-red-900/50 text-red-400',
};

export function Badge({ variant = 'default', children, className = '' }: BadgeProps) {
  return (
    <span
      className={`inline-flex items-center gap-1 rounded-full px-2 py-0.5 text-xs font-medium
        ${variantClasses[variant]} ${className}`}
    >
      {children}
    </span>
  );
}
