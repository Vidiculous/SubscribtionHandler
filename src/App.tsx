import { SubscriptionProvider } from './context/SubscriptionContext';
import { NotificationProvider } from './context/NotificationContext';
import { AppShell } from './components/layout/AppShell';

export default function App() {
  return (
    <SubscriptionProvider>
      <NotificationProvider>
        <AppShell />
      </NotificationProvider>
    </SubscriptionProvider>
  );
}
