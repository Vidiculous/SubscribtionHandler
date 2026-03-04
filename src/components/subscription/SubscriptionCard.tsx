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

  const initials = subscription.name
    .split(' ')
    .slice(0, 2)
    .map((w) => w[0])
    .join('')
    .toUpperCase();

  return (
    <>
      <div
        className="flex items-start gap-3 px-4 py-3 bg-slate-900 hover:bg-slate-800/60 transition-colors group"
        style={{ borderLeft: `4px solid ${borderColor}` }}
      >
        {/* Avatar */}
        <div
          className="w-8 h-8 rounded-md flex items-center justify-center text-white text-xs font-bold shrink-0 mt-0.5"
          style={{ backgroundColor: subscription.color }}
        >
          {initials}
        </div>

        {/* Content column */}
        <div className="flex-1 min-w-0">
          {/* Sub-row 1: name + cost + actions */}
          <div className="flex items-center justify-between gap-2">
            <div className="flex items-center gap-2 min-w-0">
              <span className="font-semibold text-slate-100 truncate text-sm">
                {subscription.name}
              </span>
              {subscription.autoRenew ? (
                <span className="text-xs text-emerald-400 shrink-0">Auto</span>
              ) : (
                <span className="text-xs text-amber-400 shrink-0">Manual</span>
              )}
            </div>
            <div className="flex items-center gap-2 shrink-0">
              <span className="text-sm text-slate-400">{formatSubscriptionCost(subscription)}</span>
              {/* Action buttons */}
              <div className="flex items-center gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
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
          </div>

          {/* Sub-row 2: badges + date + renew button */}
          <div className="flex items-center gap-2 mt-1.5 flex-wrap">
            <Badge variant={urgencyBadgeVariant[urgency]}>
              {formatDaysUntil(daysUntil)}
            </Badge>
            <Badge variant="default">
              {BILLING_CYCLE_LABELS[subscription.billingCycle]}
            </Badge>
            <span className="text-xs text-slate-500">
              {formatRenewalDate(subscription.nextRenewalDate)}
              {subscription.reminderDays > 0 && (
                <span className="ml-1">· 🔔 {subscription.reminderDays}d</span>
              )}
            </span>
            {!subscription.autoRenew && <RenewButton subscription={subscription} />}
          </div>
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
