FROM archlinux:latest

# Change default shell to Bash for better feature support/easier usage
SHELL [ "/bin/bash", "-c" ]
ENV SHELL=/bin/bash

WORKDIR /var/rinha

COPY . /var/rinha

RUN pacman -Syyyy && \
    pacman -S --noconfirm gcc unzip libxcrypt-compat && \
    curl -L https://github.com/lucasnlm/rinha-compiler-kotlin/releases/download/1.2.4/rinha-compiler-kotlin-linux-64.zip > kotlin-rinha.zip  && \
    unzip -o kotlin-rinha.zip  && \
    rm kotlin-rinha.zip

RUN chmod +x ./docker/entrypoint.sh

ENTRYPOINT ["./docker/entrypoint.sh"]
