# Payment Order Creation Implementation

## Overview
Implemented complete order creation functionality when users click "Place Order" in payment.jsp. The system now properly handles both Cash on Delivery (COD) and MoMo e-wallet payments with full data persistence.

## Changes Made

### 1. **PaymentProcessServlet.java** - Enhanced
Updated the payment processing servlet to:
- ✅ Validate address selection
- ✅ Retrieve and validate promotion codes
- ✅ Get full Address object from database using AddressDao
- ✅ Calculate totals including promotion discounts
- ✅ For **COD payments**: Create order immediately and save to database
- ✅ For **MoMo payments**: Store order details in session for callback processing

#### COD Payment Flow:
```
User clicks "Place Order" (COD)
  → Validate inputs (address, cart)
  → Get Address entity from database
  → Get Promotion entity (if promo code provided)
  → Create Order object with:
     • User
     • Address
     • Promotion
     • Payment method: "CASH_ON_DELIVERY"
     • Status: PROCESSING
     • Total amount (subtotal + shipping - discount)
  → Create OrderItems from CartItems
  → Save to database via OrderService
  → Clear cart
  → Redirect to order tracking page with success message
```

#### MoMo Payment Flow:
```
User clicks "Place Order" (MoMo)
  → Validate inputs
  → Store order details in session:
     • Cart items
     • Address entity
     • Promotion entity
     • Notes
  → Redirect to MoMo payment page
  → User completes payment on MoMo
  → MoMo redirects to callback URL
```

### 2. **MoMoCallbackServlet.java** - New File
Created new servlet to handle MoMo payment callbacks:
- ✅ Receives callback from MoMo after payment
- ✅ Checks `resultCode` parameter:
  - **Success (resultCode=0)**: Create order in database
  - **Failure**: Show error message, don't create order
- ✅ Retrieves order details from session
- ✅ Creates Order with full information:
  - User
  - Address (from session)
  - Promotion (from session)
  - Payment method: "MOMO"
  - Status: PROCESSING
- ✅ Creates OrderItems from cart
- ✅ Saves to database
- ✅ Clears cart after successful order
- ✅ Clears session data
- ✅ Redirects to order tracking page

### 3. **PaymentServlet.java** - Updated
Modified to handle success/error messages from session:
- ✅ Display payment errors from failed MoMo transactions
- ✅ Display success messages from successful orders
- ✅ Clean up session attributes after displaying

### 4. **MoMoIpnServlet.java** - Enhanced
Improved IPN (Instant Payment Notification) handler:
- ✅ Parse JSON payload from MoMo
- ✅ Log payment status
- ✅ Acknowledge receipt with 204 status code

### 5. **order-tracking.jsp** - Enhanced
Added success/error message display:
- ✅ Show success message after order creation
- ✅ Display order ID
- ✅ Show error messages if payment fails
- ✅ Auto-remove messages from session after display

## Key Features

### ✅ Full Data Persistence
- **User**: Retrieved from session
- **Address**: Full Address entity loaded from database using `AddressDao.findByIdAndUserId()`
- **Promotion**: Full Promotion entity loaded using `PromotionDAO.getPromotionByCode()` (only if code provided and valid)
- **Order Status**: Set to `PROCESSING` for both COD and MoMo
- **Payment Method**: 
  - `"CASH_ON_DELIVERY"` for COD
  - `"MOMO"` for MoMo payments
- **Total Amount**: Calculated including shipping fee and discount

### ✅ Order Items Creation
- OrderItems created from CartItems
- Book reference maintained
- Quantity preserved
- Price calculated in `OrderDAO.createOrder()` (discounted price)

### ✅ Cart Management
- Cart cleared after successful order creation
- Not cleared if order creation fails

### ✅ Session Management
- Pending order data stored in session for MoMo flow
- Cleaned up after successful order or failure
- Success/error messages passed via session

### ✅ Error Handling
- Validation for required fields (payment method, address)
- Empty cart check
- Address ownership validation
- Promotion code validation
- Database error handling
- MoMo API error handling

## Database Schema Used

### Order Table Fields:
- `id` - Auto-generated
- `userId` - Foreign key to User
- `addressId` - Foreign key to Address (full entity)
- `promotionId` - Foreign key to Promotion (nullable, full entity)
- `paymentMethod` - String ("CASH_ON_DELIVERY" or "MOMO")
- `status` - Enum (OrderStatus.PROCESSING)
- `totalAmount` - Double
- `createdAt` - Timestamp

### OrderItem Table Fields:
- `id` - Auto-generated
- `orderId` - Foreign key to Order
- `bookId` - Foreign key to Book
- `quantity` - Integer
- `price` - Double (price at purchase time)

## User Flow

### COD Flow:
1. User fills payment form and selects COD
2. Clicks "Place Order"
3. System validates and creates order immediately
4. Cart is cleared
5. User redirected to order tracking page
6. Success message displayed

### MoMo Flow:
1. User fills payment form and selects MoMo
2. Clicks "Place Order"
3. System stores order data in session
4. User redirected to MoMo payment page
5. User completes payment on MoMo
6. **If payment successful**:
   - MoMo redirects to callback servlet
   - Order created in database
   - Cart cleared
   - User sees success message
7. **If payment failed**:
   - MoMo redirects to callback servlet
   - No order created
   - Cart remains
   - User sees error message

## Testing Checklist

- [ ] COD order creation with valid data
- [ ] COD order with promotion code
- [ ] COD order without promotion code
- [ ] MoMo successful payment creates order
- [ ] MoMo failed payment doesn't create order
- [ ] Address validation works
- [ ] Empty cart prevention works
- [ ] Cart cleared after successful order
- [ ] Success message displayed on order tracking page
- [ ] Error messages displayed properly
- [ ] Order appears in order tracking page
- [ ] Order appears in admin order management

## Files Modified/Created

### Modified:
- `controller/PaymentProcessServlet.java`
- `controller/PaymentServlet.java`
- `controller/MoMoIpnServlet.java`
- `WEB-INF/views/order-tracking.jsp`

### Created:
- `controller/MoMoCallbackServlet.java`

## Dependencies Used

### Existing Services:
- `OrderService` - Order creation
- `CartService` - Cart management
- `MoMoService` - MoMo payment API
- `AddressDao` - Address retrieval
- `PromotionDAO` - Promotion retrieval

### Models:
- `Order` - Order entity
- `OrderItem` - Order item entity
- `OrderStatus` - Order status enum (PROCESSING)
- `Address` - Address entity
- `Promotion` - Promotion entity
- `User` - User entity
- `Book` - Book entity
- `Cart` - Cart entity
- `CartItem` - Cart item entity

## Notes

1. **COD orders** are created immediately upon form submission
2. **MoMo orders** are only created after successful payment confirmation
3. All orders start with status `PROCESSING`
4. Cart is automatically cleared after successful order creation
5. Full entities (Address, Promotion) are stored with orders for data integrity
6. Promotion is optional - order can be created without it
7. Session data is properly cleaned up to prevent memory leaks
