export type BillingCycle = 'weekly' | 'monthly' | 'yearly' | 'custom';
export type CustomCycleUnit = 'days' | 'weeks' | 'months';

export interface Subscription {
  id: string;
  name: string;
  cost: number;
  currency: string;
  billingCycle: BillingCycle;
  customCycleAmount?: number;
  customCycleUnit?: CustomCycleUnit;
  nextRenewalDate: string; // 'yyyy-MM-dd'
  autoRenew: boolean;
  reminderDays: number; // 0 = no reminder
  color: string; // hex
  notes?: string;
  createdAt: string;
  updatedAt: string;
}

export interface FiredNotification {
  key: string; // `${id}-${nextRenewalDate}`
  firedAt: string;
}

export interface AppState {
  subscriptions: Subscription[];
  firedNotifications: FiredNotification[];
  schemaVersion: number;
}

export type SubscriptionAction =
  | { type: 'ADD_SUBSCRIPTION'; payload: Subscription }
  | { type: 'UPDATE_SUBSCRIPTION'; payload: Subscription }
  | { type: 'DELETE_SUBSCRIPTION'; payload: { id: string } }
  | { type: 'RENEW_SUBSCRIPTION'; payload: { id: string } }
  | { type: 'MARK_NOTIFICATION_FIRED'; payload: FiredNotification }
  | { type: 'HYDRATE'; payload: AppState };

export interface SpendSummary {
  monthly: number;
  yearly: number;
  currency: string;
}

export interface UpcomingRenewal {
  subscription: Subscription;
  daysUntil: number;
}
