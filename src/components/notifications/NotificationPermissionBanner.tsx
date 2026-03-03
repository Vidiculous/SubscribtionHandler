import { useState } from 'react';
import { useNotifications } from '../../context/NotificationContext';

export function NotificationPermissionBanner() {
  const { permission, requestPermission, isSupported } = useNotifications();
  const [dismissed, setDismissed] = useState(false);

  if (!isSupported || permission !== 'default' || dismissed) return null;

  return (
    <div className="bg-amber-950/50 border-b border-amber-800 px-4 py-3">
      <div className="max-w-5xl mx-auto flex items-center justify-between gap-4">
        <div className="flex items-center gap-3">
          <svg
            className="w-5 h-5 text-amber-400 shrink-0"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
            strokeWidth={2}
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              d="M14.857 17.082a23.848 23.848 0 0 0 5.454-1.31A8.967 8.967 0 0 1 18 9.75V9A6 6 0 0 0 6 9v.75a8.967 8.967 0 0 1-2.312 6.022c1.733.64 3.56 1.085 5.455 1.31m5.714 0a24.255 24.255 0 0 1-5.714 0m5.714 0a3 3 0 1 1-5.714 0"
            />
          </svg>
          <p className="text-sm text-amber-300">
            Enable notifications to get reminders before subscriptions renew.
          </p>
        </div>
        <div className="flex items-center gap-2 shrink-0">
          <button
            onClick={() => requestPermission()}
            className="text-sm font-medium bg-amber-600 text-white px-3 py-1.5 rounded-lg
              hover:bg-amber-700 transition-colors"
          >
            Enable
          </button>
          <button
            onClick={() => setDismissed(true)}
            className="text-sm text-amber-400 hover:text-amber-200 px-2 py-1.5 rounded-lg
              hover:bg-amber-900/30 transition-colors"
            aria-label="Dismiss notification banner"
          >
            Dismiss
          </button>
        </div>
      </div>
    </div>
  );
}
