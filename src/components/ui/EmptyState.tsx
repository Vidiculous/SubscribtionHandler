interface EmptyStateProps {
  onAdd: () => void;
}

export function EmptyState({ onAdd }: EmptyStateProps) {
  return (
    <div className="flex flex-col items-center justify-center py-24 text-center">
      <div className="w-20 h-20 rounded-full bg-indigo-900/30 flex items-center justify-center mb-6">
        <svg
          className="w-10 h-10 text-indigo-400"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          strokeWidth={1.5}
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            d="M2.25 8.25h19.5M2.25 9h19.5m-16.5 5.25h6m-6 2.25h3m-3.75 3h15a2.25 2.25 0 0 0 2.25-2.25V6.75A2.25 2.25 0 0 0 19.5 4.5h-15a2.25 2.25 0 0 0-2.25 2.25v10.5A2.25 2.25 0 0 0 4.5 19.5Z"
          />
        </svg>
      </div>
      <h3 className="text-xl font-semibold text-slate-200 mb-2">No subscriptions yet</h3>
      <p className="text-slate-400 mb-8 max-w-sm">
        Start tracking your subscriptions to get a clear overview of your spending.
      </p>
      <button
        onClick={onAdd}
        className="inline-flex items-center gap-2 bg-indigo-600 text-white px-6 py-3 rounded-xl
          font-medium hover:bg-indigo-500 transition-colors"
      >
        <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
          <path strokeLinecap="round" strokeLinejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
        </svg>
        Add your first subscription
      </button>
    </div>
  );
}
