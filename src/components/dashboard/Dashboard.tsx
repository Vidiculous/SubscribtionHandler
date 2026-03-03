import type { Subscription } from '../../types';
import { useSubscriptions } from '../../context/SubscriptionContext';
import { SpendSummary } from './SpendSummary';
import { UpcomingRenewals } from './UpcomingRenewals';
import { SubscriptionGrid } from './SubscriptionGrid';

interface DashboardProps {
  onAdd: () => void;
  onEdit: (sub: Subscription) => void;
}

export function Dashboard({ onAdd, onEdit }: DashboardProps) {
  const { state, spendSummary, upcomingRenewals } = useSubscriptions();

  return (
    <main className="max-w-5xl mx-auto px-4 py-8">
      {state.subscriptions.length > 0 && (
        <SpendSummary summary={spendSummary} count={state.subscriptions.length} />
      )}
      <UpcomingRenewals renewals={upcomingRenewals} />
      <SubscriptionGrid
        subscriptions={state.subscriptions}
        onEdit={onEdit}
        onAdd={onAdd}
      />
    </main>
  );
}
