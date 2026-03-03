import { useState } from 'react';
import type { Subscription } from '../../types';
import { useSubscriptions } from '../../context/SubscriptionContext';
import { getDaysUntilRenewal, formatRenewalDate, formatDaysUntil, getUrgency } from '../../utils/dateUtils';
import { formatSubscriptionCost } from '../../utils/costUtils';
import { Badge } from '../ui/Badge';
import { ConfirmDialog } from '../ui/ConfirmDialog';
import { RenewButton } from './RenewButton';
import { BILLING_CYCLE_LABELS } from '../../constants';

interface SubscriptionCardProps {
  subscription: Subscription;
  onEdit: (sub: Subscription) => void;
}

const urgencyBadgeVariant = {
  overdue: 'overdue',
  critical: 'critical',
  warning: 'warning',
  upcoming: 'upcoming',
  ok: 'ok',
} as const;

const urgencyBorderColor = {
  overdue: '#ef4444',
  critical: '#f97316',
  warning: '#f59e0b',
  upcoming: '#6366f1',
  ok: '',
};

export function SubscriptionCard({ subscription, onEdit }: SubscriptionCardProps) {
  const { dispatch } = useSubscriptions();
  const [showDelete, setShowDelete] = useState(false);

  const daysUntil = getDaysUntilRenewal(subscription.nextRenewalDate);
  const urgency = getUrgency(daysUntil);
  const borderColor = urgencyBorderColor[urgency] || subscription.color;

  // Get first letter(s) for the avatar
  const initials = subscription.name
    .split(' ')
    .slice(0, 2)
    .map((w) => w[0])
    .join('')
    .toUpperCase();

  return (
    <>
      <div
        className="bg-slate-900 rounded-xl border border-slate-700 overflow-hidden
          hover:border-slate-600 hover:shadow-lg transition-all group"
        style={{ borderLeft: `4px solid ${borderColor}` }}
      >
        <div className="p-4">
          {/* Top row: Avatar + name + actions */}
          <div className="flex items-start justify-between gap-3 mb-3">
            <div className="flex items-center gap-3 min-w-0">
              {/* Avatar */}
              <div
                className="w-10 h-10 rounded-lg flex items-center justify-center
                  text-white text-sm font-bold shrink-0"
                style={{ backgroundColor: subscription.color }}
              >
                {initials}
              </div>
              {/* Name + badge */}
              <div className="min-w-0">
                <h3 className="font-semibold text-slate-100 truncate text-[15px] leading-tight">
                  {subscription.name}
                </h3>
                <p className="text-slate-400 text-sm mt-0.5">
                  {formatSubscriptionCost(subscription)}
                </p>
              </div>
            </div>

            {/* Action buttons */}
            <div className="flex items-center gap-1 shrink-0 opacity-60 group-hover:opacity-100 transition-opacity">
              <button
                onClick={() => onEdit(subscription)}
                className="w-7 h-7 flex items-center justify-center rounded-lg
                  text-slate-500 hover:text-indigo-400 hover:bg-indigo-900/30 transition-colors"
                aria-label={`Edit ${subscription.name}`}
              >
                <svg className="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125"
                  />
                </svg>
              </button>
              <button
                onClick={() => setShowDelete(true)}
                className="w-7 h-7 flex items-center justify-center rounded-lg
                  text-slate-500 hover:text-red-400 hover:bg-red-900/30 transition-colors"
                aria-label={`Delete ${subscription.name}`}
              >
                <svg className="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0"
                  />
                </svg>
              </button>
            </div>
          </div>

          {/* Renewal info */}
          <div className="flex items-center justify-between">
            <div>
              <div className="flex items-center gap-2 flex-wrap">
                <Badge variant={urgencyBadgeVariant[urgency]}>
                  {formatDaysUntil(daysUntil)}
                </Badge>
                <Badge variant="default">
                  {BILLING_CYCLE_LABELS[subscription.billingCycle]}
                </Badge>
              </div>
              <p className="text-xs text-slate-500 mt-1.5">
                {formatRenewalDate(subscription.nextRenewalDate)}
                {subscription.reminderDays > 0 && (
                  <span className="ml-1.5">
                    · 🔔 {subscription.reminderDays}d
                  </span>
                )}
              </p>
            </div>
          </div>

          {/* Manual renew button (non-auto-renew only) */}
          {!subscription.autoRenew && (
            <div className="mt-3 pt-3 border-t border-slate-700 flex items-center justify-between">
              <span className="text-xs text-amber-400 font-medium flex items-center gap-1">
                <svg className="w-3 h-3" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126ZM12 15.75h.007v.008H12v-.008Z"
                  />
                </svg>
                Manual renewal
              </span>
              <RenewButton subscription={subscription} />
            </div>
          )}

          {/* Auto-renew indicator */}
          {subscription.autoRenew && (
            <div className="mt-2.5 flex items-center gap-1">
              <svg
                className="w-3 h-3 text-emerald-400"
                fill="currentColor"
                viewBox="0 0 20 20"
              >
                <path
                  fillRule="evenodd"
                  d="M10 18a8 8 0 1 0 0-16 8 8 0 0 0 0 16Zm3.857-9.809a.75.75 0 0 0-1.214-.882l-3.483 4.79-1.88-1.88a.75.75 0 1 0-1.06 1.061l2.5 2.5a.75.75 0 0 0 1.137-.089l4-5.5Z"
                  clipRule="evenodd"
                />
              </svg>
              <span className="text-xs text-emerald-400 font-medium">Auto-renews</span>
            </div>
          )}
        </div>
      </div>

      <ConfirmDialog
        isOpen={showDelete}
        onClose={() => setShowDelete(false)}
        onConfirm={() => dispatch({ type: 'DELETE_SUBSCRIPTION', payload: { id: subscription.id } })}
        title="Delete Subscription"
        message={`Are you sure you want to delete "${subscription.name}"? This action cannot be undone.`}
        confirmLabel="Delete"
      />
    </>
  );
}
