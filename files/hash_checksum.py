import hashlib
import os

# Function to compute different hash digests
def compute_hashes(file_path):
    hashes = {
        "MD5": hashlib.md5(),
        "SHA1": hashlib.sha1(),
        "SHA256": hashlib.sha256(),
        "SHA512": hashlib.sha512()
    }
    
    with open(file_path, "rb") as f:
        while chunk := f.read(4096):
            for h in hashes.values():
                h.update(chunk)
    
    return {name: h.hexdigest() for name, h in hashes.items()}


# Save all hashes to hash_report.txt
def generate_hash_report(file_path):
    hashes = compute_hashes(file_path)
    with open("hash_report.txt", "w") as report:
        report.write(f"Hash Report for {file_path}\n")
        report.write("-" * 50 + "\n")
        for algo, digest in hashes.items():
            report.write(f"{algo}: {digest}\n")
    print("[+] Hash report generated: hash_report.txt")
    return hashes


# Create checksum file (only SHA-256 used here)
def create_checksum_file(file_path):
    sha256_hash = compute_hashes(file_path)["SHA256"]
    checksum_filename = f"{file_path}.sha256"
    with open(checksum_filename, "w") as f:
        f.write(f"{sha256_hash}  {os.path.basename(file_path)}\n")
    print(f"[+] Checksum file created: {checksum_filename}")


# Verify file integrity using checksum file
def verify_checksum(file_path):
    checksum_filename = f"{file_path}.sha256"
    if not os.path.exists(checksum_filename):
        print("[-] No checksum file found.")
        return
    
    with open(checksum_filename, "r") as f:
        stored_hash, filename = f.read().strip().split("  ")
    
    current_hash = compute_hashes(file_path)["SHA256"]
    if stored_hash == current_hash:
        print("[✓] Checksum OK (Authentic)")
    else:
        print("[✗] Checksum FAILED (Tampered)")


if __name__ == "__main__":
    test_file = "example.txt"   
    
    print("\n--- Hash Generation ---")
    generate_hash_report(test_file)

    print("\n--- Checksum Creation ---")
    create_checksum_file(test_file)

    print("\n--- Verification Before Tampering ---")
    verify_checksum(test_file)

    print("\n[*] Now modify the file slightly (e.g., add a character) and run verify_checksum() again to see tampering detection.\n")
