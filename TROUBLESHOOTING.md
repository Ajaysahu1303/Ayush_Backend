# Troubleshooting Guide

## Registration Page Not Showing

If clicking "Get Started" doesn't show the registration page, follow these steps:

### 1. Check Browser Console
1. Open browser developer tools (F12)
2. Go to Console tab
3. Look for any error messages
4. You should see "Register component loaded" when you visit the registration page

### 2. Verify URLs
- Homepage: http://localhost:3000
- Registration page: http://localhost:3000/register
- Login page: http://localhost:3000/login

### 3. Check if Servers are Running

#### Backend (Spring Boot)
- Should be running on http://localhost:8080
- Check terminal for Spring Boot startup messages
- If not running, start with: `./mvnw spring-boot:run`

#### Frontend (React)
- Should be running on http://localhost:3000
- Check terminal for React startup messages
- If not running, start with: `cd frontend && npm start`

### 4. Test Direct Navigation
Try navigating directly to: http://localhost:3000/register

### 5. Check Network Tab
1. Open browser developer tools
2. Go to Network tab
3. Click "Get Started" button
4. Look for any failed requests

### 6. Common Issues

#### Issue: "Register component loaded" not showing in console
**Solution**: React app not running or component not loading

#### Issue: Page shows "Registration form will go here"
**Solution**: Old version of Register.js is cached - restart React server

#### Issue: CORS errors in console
**Solution**: Backend not running or CORS not configured properly

#### Issue: "Cannot GET /register" error
**Solution**: React Router not working - check App.js routing setup

### 7. Manual Test Steps

1. **Start Backend:**
   ```bash
   ./mvnw spring-boot:run
   ```

2. **Start Frontend:**
   ```bash
   cd frontend
   npm start
   ```

3. **Test Navigation:**
   - Go to http://localhost:3000
   - Click "Get Started" button
   - Should navigate to registration form

4. **Test Registration:**
   - Fill out the form
   - Submit
   - Check console for debug messages

### 8. Expected Behavior

When working correctly:
- Clicking "Get Started" should navigate to a form with fields for:
  - Category (dropdown)
  - Organization Phone
  - Organization Email
  - Username
  - Trainer Name
  - Password
  - Confirm Password
  - Country, State, City
  - Pin Code
  - Address

### 9. Still Not Working?

1. Clear browser cache
2. Restart both servers
3. Check if ports 3000 and 8080 are available
4. Try a different browser
5. Check if MySQL is running (for backend)

### 10. Debug Information

Add this to your browser console to test:
```javascript
// Test if React Router is working
console.log('Current URL:', window.location.href);

// Test if axios is available
console.log('Axios available:', typeof axios !== 'undefined');
``` 