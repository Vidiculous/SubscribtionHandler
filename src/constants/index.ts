import type { BillingCycle, CustomCycleUnit } from '../types';

export const BILLING_CYCLES: BillingCycle[] = ['weekly', 'monthly', 'yearly', 'custom'];

export const CUSTOM_CYCLE_UNITS: CustomCycleUnit[] = ['days', 'weeks', 'months'];

export const BILLING_CYCLE_LABELS: Record<BillingCycle, string> = {
  weekly: 'Weekly',
  monthly: 'Monthly',
  yearly: 'Yearly',
  custom: 'Custom',
};

export const DEFAULT_REMINDER_DAYS = 7;

export const PRESET_COLORS = [
  '#6366f1', // indigo
  '#ec4899', // pink
  '#f59e0b', // amber
  '#22c55e', // green
  '#06b6d4', // cyan
  '#8b5cf6', // violet
  '#ef4444', // red
  '#14b8a6', // teal
  '#f97316', // orange
  '#64748b', // slate
];

export const STORAGE_KEY = 'subscriptionHandler_v1';
export const SCHEMA_VERSION = 1;

export const COMMON_CURRENCIES = ['USD', 'EUR', 'GBP', 'SEK', 'NOK', 'DKK', 'CHF', 'JPY', 'CAD', 'AUD'];
