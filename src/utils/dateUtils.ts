import {
  differenceInCalendarDays,
  addWeeks,
  addMonths,
  addYears,
  addDays,
  parseISO,
  format,
} from 'date-fns';
import type { Subscription } from '../types';

export function getDaysUntilRenewal(nextRenewalDate: string): number {
  return differenceInCalendarDays(parseISO(nextRenewalDate), new Date());
}

export function advanceByOneCycle(sub: Subscription): string {
  const base = parseISO(sub.nextRenewalDate);
  let next: Date;

  switch (sub.billingCycle) {
    case 'weekly':
      next = addWeeks(base, 1);
      break;
    case 'monthly':
      next = addMonths(base, 1);
      break;
    case 'yearly':
      next = addYears(base, 1);
      break;
    case 'custom': {
      const amount = sub.customCycleAmount ?? 1;
      const unit = sub.customCycleUnit ?? 'months';
      if (unit === 'days') next = addDays(base, amount);
      else if (unit === 'weeks') next = addWeeks(base, amount);
      else next = addMonths(base, amount);
      break;
    }
  }

  return format(next, 'yyyy-MM-dd');
}

export function formatRenewalDate(dateStr: string): string {
  return format(parseISO(dateStr), 'MMM d, yyyy');
}

export function formatDaysUntil(days: number): string {
  if (days < 0) return `${Math.abs(days)} day${Math.abs(days) !== 1 ? 's' : ''} overdue`;
  if (days === 0) return 'Today';
  if (days === 1) return 'Tomorrow';
  return `In ${days} days`;
}

export function getUrgency(days: number): 'overdue' | 'critical' | 'warning' | 'upcoming' | 'ok' {
  if (days < 0) return 'overdue';
  if (days === 0) return 'critical';
  if (days <= 3) return 'critical';
  if (days <= 7) return 'warning';
  if (days <= 14) return 'upcoming';
  return 'ok';
}
