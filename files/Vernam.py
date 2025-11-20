import secrets

def generate_key(length):
    """
    Generates a secure, random key (as bytes) of a given length.
    """
    return secrets.token_bytes(length)

def xor_encrypt_decrypt(data_bytes, key_bytes):
    """
    Performs XOR operation. This function is used for both
    encryption and decryption.
    """
    if len(key_bytes) < len(data_bytes):
        raise ValueError("Key must be at least as long as the message.")
        
    out_bytes = bytearray()
    
    # XOR each byte
    for i in range(len(data_bytes)):
        out_bytes.append(data_bytes[i] ^ key_bytes[i])
        
    return bytes(out_bytes)

# --- Main Program ---
def main_vernam():
    print("--- Vernam Cipher (One-Time Pad) ---")
    try:
        msg = input("Enter message: ")
        msg_bytes = msg.encode('utf-8')
        
        # 1. Generate a key that is the same length as the message
        #    (This is the "One-Time Pad" part)
        key = generate_key(len(msg_bytes))
        
        print(f"\nOriginal Message: {msg}")
        # Note: The key is random bytes and may not be printable.
        # We print its hex representation for demonstration.
        print(f"Random Key (hex): {key.hex()}")

        # --- Encryption ---
        # The ciphertext is also raw bytes and may not be printable
        cipher_bytes = xor_encrypt_decrypt(msg_bytes, key)
        print(f"Ciphertext (hex): {cipher_bytes.hex()}")

        # --- Decryption ---
        # The (C ^ K) operation reverses the (P ^ K) operation
        decrypted_bytes = xor_encrypt_decrypt(cipher_bytes, key)
        decrypted_msg = decrypted_bytes.decode('utf-8')
        
        print(f"Decrypted Message: {decrypted_msg}")
        
    except Exception as e:
        print(f"An error occurred: {e}")

main_vernam() # Uncomment to run