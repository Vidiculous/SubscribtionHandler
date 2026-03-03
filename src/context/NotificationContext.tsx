import { createContext, useContext, useState, useCallback, type ReactNode } from 'react';

interface NotificationContextValue {
  permission: NotificationPermission;
  requestPermission: () => Promise<void>;
  isSupported: boolean;
}

const NotificationContext = createContext<NotificationContextValue | null>(null);

export function NotificationProvider({ children }: { children: ReactNode }) {
  const isSupported = 'Notification' in window;
  const [permission, setPermission] = useState<NotificationPermission>(
    isSupported ? Notification.permission : 'denied'
  );

  const requestPermission = useCallback(async () => {
    if (!isSupported) return;
    const result = await Notification.requestPermission();
    setPermission(result);
  }, [isSupported]);

  return (
    <NotificationContext.Provider value={{ permission, requestPermission, isSupported }}>
      {children}
    </NotificationContext.Provider>
  );
}

export function useNotifications(): NotificationContextValue {
  const ctx = useContext(NotificationContext);
  if (!ctx) throw new Error('useNotifications must be used within NotificationProvider');
  return ctx;
}
