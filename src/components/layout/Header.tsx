import { useSubscriptions } from '../../context/SubscriptionContext';
import { getDaysUntilRenewal } from '../../utils/dateUtils';

interface HeaderProps {
  onAddSubscription: () => void;
}

export function Header({ onAddSubscription }: HeaderProps) {
  const { state } = useSubscriptions();

  const urgentCount = state.subscriptions.filter((sub) => {
    const days = getDaysUntilRenewal(sub.nextRenewalDate);
    return days >= 0 && days <= 3;
  }).length;

  return (
    <header className="sticky top-0 z-40 bg-slate-900/90 backdrop-blur-md border-b border-slate-700">
      <div className="max-w-5xl mx-auto px-4 h-16 flex items-center justify-between gap-4">
        {/* Logo */}
        <div className="flex items-center gap-2.5">
          <div className="w-8 h-8 rounded-lg bg-indigo-600 flex items-center justify-center">
            <svg
              className="w-4.5 h-4.5 text-white"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              strokeWidth={2.5}
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="M2.25 8.25h19.5M2.25 9h19.5m-16.5 5.25h6m-6 2.25h3m-3.75 3h15a2.25 2.25 0 0 0 2.25-2.25V6.75A2.25 2.25 0 0 0 19.5 4.5h-15a2.25 2.25 0 0 0-2.25 2.25v10.5A2.25 2.25 0 0 0 4.5 19.5Z"
              />
            </svg>
          </div>
          <span className="text-lg font-bold text-slate-100 tracking-tight">SubHandler</span>
        </div>

        {/* Actions */}
        <div className="flex items-center gap-3">
          {/* Bell with badge */}
          {urgentCount > 0 && (
            <div className="relative">
              <button
                className="w-9 h-9 flex items-center justify-center rounded-lg text-slate-400
                  hover:bg-slate-800 hover:text-slate-200 transition-colors"
                aria-label={`${urgentCount} subscription${urgentCount !== 1 ? 's' : ''} renewing soon`}
              >
                <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M14.857 17.082a23.848 23.848 0 0 0 5.454-1.31A8.967 8.967 0 0 1 18 9.75V9A6 6 0 0 0 6 9v.75a8.967 8.967 0 0 1-2.312 6.022c1.733.64 3.56 1.085 5.455 1.31m5.714 0a24.255 24.255 0 0 1-5.714 0m5.714 0a3 3 0 1 1-5.714 0"
                  />
                </svg>
              </button>
              <span
                className="absolute -top-1 -right-1 w-4.5 h-4.5 bg-red-500 text-white text-[10px]
                  font-bold rounded-full flex items-center justify-center pointer-events-none"
              >
                {urgentCount}
              </span>
            </div>
          )}

          {/* Add button */}
          <button
            onClick={onAddSubscription}
            className="inline-flex items-center gap-1.5 bg-indigo-600 text-white px-3.5 py-2
              rounded-lg text-sm font-medium hover:bg-indigo-500 active:bg-indigo-700
              transition-colors focus-visible:outline-none focus-visible:ring-2
              focus-visible:ring-indigo-500 focus-visible:ring-offset-2 focus-visible:ring-offset-slate-900"
          >
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2.5}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
            </svg>
            <span className="hidden sm:inline">Add Subscription</span>
            <span className="sm:hidden">Add</span>
          </button>
        </div>
      </div>
    </header>
  );
}
