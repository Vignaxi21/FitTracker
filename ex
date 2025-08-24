import { render, screen, fireEvent } from '@testing-library/react';
import MyForm from './MyForm';
import validateEmail from 'email-validator';

// Mock the email-validator library to control its behavior in tests.
jest.mock('email-validator', () => ({
  validate: jest.fn(),
}));

describe('MyForm Component Validation', () => {

  // A helper function to simulate user input and form submission.
  const setupFormAndSubmit = (email, password) => {
    render(<MyForm />);
    const emailInput = screen.getByLabelText(/Email/i);
    const passwordInput = screen.getByLabelText(/Password/i);
    const submitButton = screen.getByRole('button', { name: /Submit/i });

    fireEvent.change(emailInput, { target: { value: email } });
    fireEvent.change(passwordInput, { target: { value: password } });
    fireEvent.click(submitButton);
  };

  test('should show success message on valid email and password', () => {
    // Mock the email-validator to return true for a valid email.
    validateEmail.validate.mockReturnValue(true);

    setupFormAndSubmit('test@example.com', 'ValidPass!1');

    // Check for the success message.
    expect(screen.getByText(/Success!/i)).toBeInTheDocument();
  });

  test('should show error for invalid email', () => {
    // Mock the email-validator to return false for an invalid email.
    validateEmail.validate.mockReturnValue(false);

    setupFormAndSubmit('invalid-email', 'ValidPass!1');

    // Check for the specific email error message.
    expect(screen.getByText(/Please enter a valid email address./i)).toBeInTheDocument();
  });

  test('should show error if password is less than 8 characters', () => {
    validateEmail.validate.mockReturnValue(true);

    setupFormAndSubmit('test@example.com', 'short!1');

    // Check for the specific password length error message.
    expect(screen.getByText(/Password must be at least 8 characters./i)).toBeInTheDocument();
  });

  test('should show error if password is missing an uppercase letter', () => {
    validateEmail.validate.mockReturnValue(true);

    setupFormAndSubmit('test@example.com', 'nouppercase!1');

    // Check for the specific uppercase error message.
    expect(screen.getByText(/Password must contain at least one uppercase letter./i)).toBeInTheDocument();
  });

  test('should show error if password is missing a lowercase letter', () => {
    validateEmail.validate.mockReturnValue(true);

    setupFormAndSubmit('test@example.com', 'NOLOWERCASE!1');

    // Check for the specific lowercase error message.
    expect(screen.getByText(/Password must contain at least one lowercase letter./i)).toBeInTheDocument();
  });

  test('should show error if password is missing a numerical digit', () => {
    validateEmail.validate.mockReturnValue(true);

    setupFormAndSubmit('test@example.com', 'NoNumber!@#');

    // Check for the specific number error message.
    expect(screen.getByText(/Password must contain at least one numerical digit./i)).toBeInTheDocument();
  });

  test('should show error if password is missing a special character', () => {
    validateEmail.validate.mockReturnValue(true);

    setupFormAndSubmit('test@example.com', 'NoSpecial1');

    // Check for the specific special character error message.
    expect(screen.getByText(/Password must contain at least one special character \(!@#\$%\^&\*\)\./i)).toBeInTheDocument();
  });

  test('should clear all errors on successful validation after a failed attempt', () => {
    validateEmail.validate.mockReturnValue(true);
    render(<MyForm />);

    // First, submit an invalid password
    fireEvent.change(screen.getByLabelText(/Email/i), { target: { value: 'test@example.com' } });
    fireEvent.change(screen.getByLabelText(/Password/i), { target: { value: 'invalid' } });
    fireEvent.click(screen.getByRole('button', { name: /Submit/i }));
    expect(screen.getByText(/Password must be at least 8 characters./i)).toBeInTheDocument();

    // Now, change to a valid password and resubmit
    fireEvent.change(screen.getByLabelText(/Password/i), { target: { value: 'ValidPass!1' } });
    fireEvent.click(screen.getByRole('button', { name: /Submit/i }));

    // Errors should be gone, and success message should appear.
    expect(screen.queryByText(/Password must be at least 8 characters./i)).toBeNull();
    expect(screen.getByText(/Success!/i)).toBeInTheDocument();
  });
});
