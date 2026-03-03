import { useEffect } from 'react';
import { useSubscriptions } from '../context/SubscriptionContext';
import { useNotifications } from '../context/NotificationContext';
import { getDaysUntilRenewal } from '../utils/dateUtils';
import { getNotificationKey, buildNotificationPayload } from '../utils/notificationUtils';

export function useRenewalCheck() {
  const { state, dispatch } = useSubscriptions();
  const { permission, isSupported } = useNotifications();

  useEffect(() => {
    if (!isSupported || permission !== 'granted') return;
    if (state.subscriptions.length === 0) return;

    const firedKeys = new Set(state.firedNotifications.map((f) => f.key));

    state.subscriptions.forEach((sub) => {
      if (sub.reminderDays === 0) return;

      const daysUntil = getDaysUntilRenewal(sub.nextRenewalDate);
      if (daysUntil > sub.reminderDays) return;
      if (daysUntil < 0) return;

      const key = getNotificationKey(sub);
      if (firedKeys.has(key)) return;

      const { title, body } = buildNotificationPayload(sub, daysUntil);

      new Notification(title, {
        body,
        tag: key,
        icon: '/vite.svg',
      });

      dispatch({
        type: 'MARK_NOTIFICATION_FIRED',
        payload: { key, firedAt: new Date().toISOString() },
      });
    });
    // Run once after hydration — dependency array intentionally omits state
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isSupported, permission]);
}
