with open("exfiltrated.txt", "r") as exfil:
    # Writing data to a file
    encoded_b64 = exfil.read()
    decoded_b64 = ""
    for c in encoded_b64:
        if c == " ":
            decoded_b64 += "+"
        else:
            decoded_b64 += c
    exfil.close()

with open('decoded_exfiltrated.txt', 'w') as output:
    output.write(decoded_b64)
    output.close()