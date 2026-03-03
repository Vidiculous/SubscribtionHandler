import type { UpcomingRenewal } from '../../types';
import { formatDaysUntil, getUrgency } from '../../utils/dateUtils';
import { formatSubscriptionCost } from '../../utils/costUtils';

interface UpcomingRenewalsProps {
  renewals: UpcomingRenewal[];
}

const urgencyDot: Record<ReturnType<typeof getUrgency>, string> = {
  overdue: 'bg-red-500',
  critical: 'bg-orange-500',
  warning: 'bg-yellow-500',
  upcoming: 'bg-blue-500',
  ok: 'bg-green-500',
};

export function UpcomingRenewals({ renewals }: UpcomingRenewalsProps) {
  if (renewals.length === 0) return null;

  return (
    <div className="mb-8">
      <h2 className="text-sm font-semibold text-slate-400 uppercase tracking-wide mb-3">
        Renewing this week
      </h2>
      <div className="flex gap-3 overflow-x-auto pb-2 -mx-1 px-1">
        {renewals.map(({ subscription, daysUntil }) => {
          const urgency = getUrgency(daysUntil);
          return (
            <div
              key={subscription.id}
              className="shrink-0 bg-slate-900 rounded-xl border border-slate-700 p-3 w-40"
            >
              <div className="flex items-center gap-2 mb-2">
                <div
                  className="w-7 h-7 rounded-md flex items-center justify-center
                    text-white text-xs font-bold"
                  style={{ backgroundColor: subscription.color }}
                >
                  {subscription.name[0].toUpperCase()}
                </div>
                <div
                  className={`w-2 h-2 rounded-full shrink-0 ml-auto ${urgencyDot[urgency]}`}
                  aria-hidden="true"
                />
              </div>
              <p className="font-medium text-slate-100 text-sm leading-tight truncate">
                {subscription.name}
              </p>
              <p className="text-xs text-slate-400 mt-0.5">{formatSubscriptionCost(subscription)}</p>
              <p className={`text-xs font-medium mt-1.5 ${
                urgency === 'overdue' || urgency === 'critical' ? 'text-red-400' :
                urgency === 'warning' ? 'text-yellow-400' : 'text-blue-400'
              }`}>
                {formatDaysUntil(daysUntil)}
              </p>
            </div>
          );
        })}
      </div>
    </div>
  );
}
