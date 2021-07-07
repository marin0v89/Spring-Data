package Entity.p05_payment;

import javax.persistence.*;

@Entity
@Table(name = "credit_cards")
public class CreditCard extends BillingDetail {
    @Enumerated(EnumType.STRING)
    private CardType cardType;
    @Column(name = "expiration_month")
    private Integer expMonth;
    @Column(name = "expiration_year")
    private Integer expYear;

    public CreditCard() {
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public Integer getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(Integer expMonth) {
        this.expMonth = expMonth;
    }

    public Integer getExpYear() {
        return expYear;
    }

    public void setExpYear(Integer expYear) {
        this.expYear = expYear;
    }
}
