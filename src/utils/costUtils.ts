import type { Subscription, SpendSummary } from '../types';

function toDaysInCycle(sub: Subscription): number {
  const amount = sub.customCycleAmount ?? 1;
  const unit = sub.customCycleUnit ?? 'months';
  if (unit === 'days') return amount;
  if (unit === 'weeks') return amount * 7;
  return amount * 30.4375; // average days per month
}

export function toMonthly(sub: Subscription): number {
  switch (sub.billingCycle) {
    case 'weekly':
      return sub.cost * (52 / 12);
    case 'monthly':
      return sub.cost;
    case 'yearly':
      return sub.cost / 12;
    case 'custom': {
      const daysInCycle = toDaysInCycle(sub);
      return sub.cost * (365.25 / daysInCycle / 12);
    }
  }
}

export function toYearly(sub: Subscription): number {
  return toMonthly(sub) * 12;
}

export function computeSpendSummary(subs: Subscription[]): SpendSummary {
  const currencies = new Set(subs.map((s) => s.currency));
  return {
    monthly: subs.reduce((acc, s) => acc + toMonthly(s), 0),
    yearly: subs.reduce((acc, s) => acc + toYearly(s), 0),
    currency: currencies.size === 1 ? [...currencies][0] : 'Mixed',
  };
}

export function formatCurrency(amount: number, currency: string): string {
  if (currency === 'Mixed') {
    return `~$${amount.toFixed(2)}`;
  }
  try {
    return new Intl.NumberFormat(undefined, {
      style: 'currency',
      currency,
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    }).format(amount);
  } catch {
    return `${currency} ${amount.toFixed(2)}`;
  }
}

export function formatSubscriptionCost(sub: Subscription): string {
  const cycleLabel: Record<string, string> = {
    weekly: '/week',
    monthly: '/mo',
    yearly: '/yr',
    custom: '',
  };
  const suffix = sub.billingCycle === 'custom'
    ? `/${sub.customCycleAmount ?? 1} ${sub.customCycleUnit ?? 'months'}`
    : cycleLabel[sub.billingCycle];
  return `${formatCurrency(sub.cost, sub.currency)}${suffix}`;
}
