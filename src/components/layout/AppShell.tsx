import { useState } from 'react';
import type { Subscription } from '../../types';
import { useRenewalCheck } from '../../hooks/useRenewalCheck';
import { Header } from './Header';
import { Dashboard } from '../dashboard/Dashboard';
import { NotificationPermissionBanner } from '../notifications/NotificationPermissionBanner';
import { Modal } from '../ui/Modal';
import { SubscriptionForm } from '../subscription/SubscriptionForm';

type ModalState =
  | { mode: 'closed' }
  | { mode: 'add' }
  | { mode: 'edit'; subscription: Subscription };

export function AppShell() {
  useRenewalCheck();

  const [modalState, setModalState] = useState<ModalState>({ mode: 'closed' });

  function closeModal() {
    setModalState({ mode: 'closed' });
  }

  function openAdd() {
    setModalState({ mode: 'add' });
  }

  function openEdit(subscription: Subscription) {
    setModalState({ mode: 'edit', subscription });
  }

  const isOpen = modalState.mode !== 'closed';
  const modalTitle = modalState.mode === 'edit' ? 'Edit Subscription' : 'Add Subscription';

  return (
    <div className="min-h-screen bg-slate-950">
      <Header onAddSubscription={openAdd} />
      <NotificationPermissionBanner />
      <Dashboard onAdd={openAdd} onEdit={openEdit} />

      <Modal isOpen={isOpen} onClose={closeModal} title={modalTitle}>
        {modalState.mode === 'add' && (
          <SubscriptionForm onDone={closeModal} />
        )}
        {modalState.mode === 'edit' && (
          <SubscriptionForm initial={modalState.subscription} onDone={closeModal} />
        )}
      </Modal>
    </div>
  );
}
