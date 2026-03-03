import { useState } from 'react';
import type { Subscription } from '../../types';
import { useSubscriptions } from '../../context/SubscriptionContext';

interface RenewButtonProps {
  subscription: Subscription;
}

export function RenewButton({ subscription }: RenewButtonProps) {
  const { dispatch } = useSubscriptions();
  const [confirming, setConfirming] = useState(false);

  if (!confirming) {
    return (
      <button
        onClick={() => setConfirming(true)}
        className="inline-flex items-center gap-1.5 text-xs font-medium text-emerald-400
          bg-emerald-900/30 hover:bg-emerald-900/50 px-2.5 py-1.5 rounded-lg transition-colors"
        aria-label={`Mark ${subscription.name} as renewed`}
      >
        <svg className="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2.5}>
          <path strokeLinecap="round" strokeLinejoin="round" d="m4.5 12.75 6 6 9-13.5" />
        </svg>
        Mark Renewed
      </button>
    );
  }

  return (
    <div className="flex items-center gap-1.5">
      <span className="text-xs text-slate-400">Confirm?</span>
      <button
        onClick={() => {
          dispatch({ type: 'RENEW_SUBSCRIPTION', payload: { id: subscription.id } });
          setConfirming(false);
        }}
        className="text-xs font-medium text-white bg-emerald-600 hover:bg-emerald-700
          px-2 py-1 rounded transition-colors"
      >
        Yes
      </button>
      <button
        onClick={() => setConfirming(false)}
        className="text-xs font-medium text-slate-300 hover:text-slate-100
          px-2 py-1 rounded hover:bg-slate-700 transition-colors"
      >
        No
      </button>
    </div>
  );
}
