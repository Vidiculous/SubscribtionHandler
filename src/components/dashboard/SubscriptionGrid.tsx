import type { Subscription } from '../../types';
import { getDaysUntilRenewal } from '../../utils/dateUtils';
import { SubscriptionCard } from '../subscription/SubscriptionCard';
import { EmptyState } from '../ui/EmptyState';

interface SubscriptionGridProps {
  subscriptions: Subscription[];
  onEdit: (sub: Subscription) => void;
  onAdd: () => void;
}

export function SubscriptionGrid({ subscriptions, onEdit, onAdd }: SubscriptionGridProps) {
  if (subscriptions.length === 0) {
    return <EmptyState onAdd={onAdd} />;
  }

  // Sort by days until renewal (ascending), overdue first
  const sorted = [...subscriptions].sort(
    (a, b) => getDaysUntilRenewal(a.nextRenewalDate) - getDaysUntilRenewal(b.nextRenewalDate)
  );

  return (
    <div>
      <h2 className="text-sm font-semibold text-slate-400 uppercase tracking-wide mb-3">
        All Subscriptions
      </h2>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {sorted.map((sub) => (
          <SubscriptionCard key={sub.id} subscription={sub} onEdit={onEdit} />
        ))}
      </div>
    </div>
  );
}
