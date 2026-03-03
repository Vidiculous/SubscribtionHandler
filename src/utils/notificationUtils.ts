import type { Subscription } from '../types';
import { formatCurrency } from './costUtils';

export function getNotificationKey(sub: Subscription): string {
  return `${sub.id}-${sub.nextRenewalDate}`;
}

export function buildNotificationPayload(sub: Subscription, daysUntil: number): { title: string; body: string } {
  const cost = formatCurrency(sub.cost, sub.currency);
  if (daysUntil <= 0) {
    return {
      title: `${sub.name} renews today`,
      body: `${sub.name} renews today — ${cost}`,
    };
  }
  if (daysUntil === 1) {
    return {
      title: `${sub.name} renews tomorrow`,
      body: `${sub.name} renews tomorrow — ${cost}`,
    };
  }
  return {
    title: `${sub.name} renews in ${daysUntil} days`,
    body: `${sub.name} renews in ${daysUntil} days — ${cost}`,
  };
}
