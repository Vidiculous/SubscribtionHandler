import type { SpendSummary as SpendSummaryType } from '../../types';
import { formatCurrency } from '../../utils/costUtils';

interface SpendSummaryProps {
  summary: SpendSummaryType;
  count: number;
}

export function SpendSummary({ summary, count }: SpendSummaryProps) {
  const formatAmount = (amount: number) => formatCurrency(amount, summary.currency);

  return (
    <div className="grid grid-cols-2 sm:grid-cols-3 gap-4 mb-8">
      <div className="bg-slate-900 rounded-xl border border-slate-700 p-4">
        <p className="text-xs font-medium text-slate-400 uppercase tracking-wide mb-1">
          Monthly
        </p>
        <p className="text-2xl font-bold text-slate-100 leading-none">
          {formatAmount(summary.monthly)}
        </p>
        {summary.currency === 'Mixed' && (
          <p className="text-xs text-slate-500 mt-1">Multiple currencies</p>
        )}
      </div>

      <div className="bg-slate-900 rounded-xl border border-slate-700 p-4">
        <p className="text-xs font-medium text-slate-400 uppercase tracking-wide mb-1">
          Yearly
        </p>
        <p className="text-2xl font-bold text-slate-100 leading-none">
          {formatAmount(summary.yearly)}
        </p>
      </div>

      <div className="bg-slate-900 rounded-xl border border-slate-700 p-4 col-span-2 sm:col-span-1">
        <p className="text-xs font-medium text-slate-400 uppercase tracking-wide mb-1">
          Subscriptions
        </p>
        <p className="text-2xl font-bold text-slate-100 leading-none">{count}</p>
        <p className="text-xs text-slate-500 mt-1">active</p>
      </div>
    </div>
  );
}
